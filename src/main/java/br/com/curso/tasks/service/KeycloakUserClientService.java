package br.com.curso.tasks.service;

import br.com.curso.tasks.enums.MessageException;
import br.com.curso.tasks.exception.NotFound;
import br.com.curso.tasks.service.requestclient.KeycloakUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeycloakUserClientService {

    private final WebClient webClient;
    private final KeycloakAuthService authService;

    @Value("${spring.keycloak.uri-create-user}")
    private String uriCreateUser;

    @Value("${spring.keycloak.uri-execute-email-valid-user}")
    private String uriExecuteEmailValidUser;

    @Value("${spring.keycloak.uri-reset-password}")
    private String uriresetPassword;

    public Mono<String> createUser(KeycloakUserRequest keycloakUserRequest) {
        log.info("Creating user in Keycloak with email {}", keycloakUserRequest.getEmail());
        return authService.getAccessTokenAdmin()
            .flatMap(token -> {
                log.debug("Token admin obtido, iniciando requisição de criação de usuário para {}", keycloakUserRequest.getEmail());
                log.debug("Payload de criação Keycloak (sanitized): {}", keycloakUserRequest);
                return webClient.post()
                    .uri(uriCreateUser)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(keycloakUserRequest)
                    .exchangeToMono(response -> {
                        var status = response.statusCode();
                        log.debug("Resposta do Keycloak (create user) para {}: status={}", keycloakUserRequest.getEmail(), status);
                        // success (201 CREATED or other 2xx)
                        if (status.is2xxSuccessful()) {
                            URI location = response.headers().asHttpHeaders().getLocation();
                            if (location != null) {
                                String path = location.getPath();
                                String id = path.substring(path.lastIndexOf('/') + 1);
                                log.info("User created in Keycloak with id {} (email={})", id, keycloakUserRequest.getEmail());
                                return Mono.just(id);
                            } else {
                                log.error("Location header not found in Keycloak response for user {}", keycloakUserRequest.getEmail());
                                return Mono.error(new NotFound(MessageException.HEADER_LOCATION_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));
                            }
                        }

                        // conflict - user exists
                        if (status.value() == HttpStatus.CONFLICT.value()) {
                            return response.bodyToMono(String.class)
                                .defaultIfEmpty("No body")
                                .flatMap(body -> {
                                    log.warn("Keycloak returned 409 Conflict when creating user {}: {}", keycloakUserRequest.getEmail(), body);
                                    return Mono.error(new IllegalStateException("Usuário já existe no Keycloak: " + body));
                                });
                        }

                        // other errors - include body for debugging
                        return response.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .flatMap(body -> {
                                log.error("Keycloak create user failed. status: {}, body: {}", status, body);
                                return Mono.error(new IllegalStateException("Keycloak error: " + status));
                            });
                    });
            });
    }

    public Mono<Void> executeActionsEmail(String userId, List<String> actions) {
        log.info("Executando ações de e-mail no Keycloak para userId={} actions={}", userId, actions);
        return authService.getAccessTokenAdmin()
            .flatMap(token -> webClient.put()
                .uri(uriExecuteEmailValidUser, userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(actions)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(v -> log.debug("execute-actions-email sucesso para userId={}", userId))
                .doOnError(err -> log.warn("Falha em execute-actions-email userId={}: {}", userId, err.getMessage()))
            );
    }
}
