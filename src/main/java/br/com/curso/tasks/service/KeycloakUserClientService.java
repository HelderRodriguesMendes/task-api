package br.com.curso.tasks.service;

import br.com.curso.tasks.enums.MessageException;
import br.com.curso.tasks.exception.NotFound;
import br.com.curso.tasks.service.requestclient.KeycloakUserRequest;
import br.com.curso.tasks.service.requestclient.ResetPasswordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Service
public class KeycloakUserClientService {

    private final WebClient webClient;
    private final KeycloakAuthService authService;

    @Value("${spring.keycloak.uri-create-user}")
    private String uriCreateUser;

    @Value("${spring.keycloak.uri-reset-password}")
    private String uriresetPassword;

    public Mono<String> createUser(KeycloakUserRequest keycloakUserRequest) {
        log.info("Creating user in Keycloak with email {}", keycloakUserRequest.getEmail());
        return authService.getAccessTokenAdmin()
            .flatMap(token -> webClient.post()
                .uri(uriCreateUser)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(keycloakUserRequest)
                .exchangeToMono(response -> {
                    HttpStatus status = (HttpStatus) response.statusCode();
                    if (status.is2xxSuccessful() || status == HttpStatus.CREATED) {
                        URI location = response.headers().asHttpHeaders().getLocation();
                        if (location != null) {
                            String path = location.getPath();
                            String id = path.substring(path.lastIndexOf('/') + 1);
                            log.info("User created in Keycloak with id {}", id);
                            return Mono.just(id);
                        } else {
                            log.error("Location header not found in Keycloak response for user {}", keycloakUserRequest.getEmail());
                            return Mono.error(new NotFound(MessageException.HEADER_LOCATION_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));
                        }
                    } else if (status == HttpStatus.CONFLICT) {
                        return response.bodyToMono(String.class)
                            .defaultIfEmpty("No body")
                            .flatMap(body -> {
                                log.warn("Keycloak returned 409 Conflict when creating user {}: {}", keycloakUserRequest.getEmail(), body);
                                return Mono.error(new IllegalStateException("Usuário já existe no Keycloak: " + body));
                            });
                    } else {
                        return response.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .flatMap(body -> {
                                log.error("Keycloak create user failed. status: {}, body: {}", status, body);
                                return Mono.error(new IllegalStateException("Keycloak error: " + status));
                            });
                    }
                })
            );
    }

    public Mono<Void> resetPassword(String userId, ResetPasswordRequest passwordRequest) {
        return authService.getAccessTokenAdmin()
            .flatMap(token -> webClient.put()
                .uri(uriresetPassword, userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(passwordRequest)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        log.info("Password reset successful for userId {}", userId);
                        return Mono.empty();
                    } else {
                        return response.bodyToMono(String.class)
                            .defaultIfEmpty("")
                            .flatMap(body -> {
                                log.error("Keycloak reset password failed. status: {}, body: {}", response.statusCode(), body);
                                return Mono.error(new IllegalStateException("Keycloak error: " + response.statusCode()));
                            });
                    }
                })
            );
    }
}
