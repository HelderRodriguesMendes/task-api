package br.com.curso.tasks.service;

import br.com.curso.tasks.enums.MessageException;
import br.com.curso.tasks.exception.NotFound;
import br.com.curso.tasks.service.requestclient.KeycloakUserRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class KeycloakUserClientService {

    private final WebClient webClient;
    private final KeycloakAuthService authService;

    @Value("${spring.keykloak.uri-create-user}")
    private String uriCreateUser;

    public KeycloakUserClientService(@Value("${spring.keycloak.endpoint}") String keycloakEndpoint) {

        this.webClient = WebClient.builder()
            .baseUrl(keycloakEndpoint)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
        this.authService = new KeycloakAuthService(keycloakEndpoint);
    }

    public Mono<String> createUser(KeycloakUserRequest keycloakUserRequest) {
        return authService.getAccessTokenAdmin()
            .flatMap((String token) -> webClient.post()
                .uri(uriCreateUser)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(keycloakUserRequest)
                .retrieve()
                .toBodilessEntity()
                .map((ResponseEntity<Void> response) -> {
                    URI location = response.getHeaders().getLocation();
                    if (location != null) {
                        String path = location.getPath();
                        return path.substring(path.lastIndexOf('/') + 1);
                    } else {
                        throw new NotFound(MessageException.HEADER_LOCATION_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
                    }
                })
            );
    }
}