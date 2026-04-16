package br.com.curso.tasks.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Service
public class KeycloakAuthService {

    private final WebClient webClient;
    private final String endpoint;
    private final String clientId;
    private final String username;
    private final String password;
    private final String grantType;
    private final String uriAccessTokenAdmin;

    public KeycloakAuthService(
        WebClient webClient,
        @Value("${spring.keycloak.endpoint}") String endpoint,
        @Value("${spring.keycloak.id}") String clientId,
        @Value("${spring.keycloak.username-keycloak}") String username,
        @Value("${spring.keycloak.password-keycloak}") String password,
        @Value("${spring.keycloak.grant-type}") String grantType,
        @Value("${spring.keycloak.uri-access-token-admin}") String uriAccessTokenAdmin
    ) {
        this.webClient = webClient;
        this.endpoint = endpoint;
        this.clientId = clientId;
        this.username = username;
        this.password = password;
        this.grantType = grantType;
        this.uriAccessTokenAdmin = uriAccessTokenAdmin;
    }

    public Mono<String> getAccessTokenAdmin() {
        String url = endpoint + uriAccessTokenAdmin;

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("username", username);
        form.add("password", password);
        form.add("grant_type", grantType);

        return webClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(form))
            .retrieve()
            .bodyToMono(Map.class)
            .map(map -> {
                Object token = map.get("access_token");
                if (token == null) {
                    throw new IllegalStateException("access_token não encontrado na resposta do Keycloak");
                }
                return token.toString();
            })
            .doOnError(err -> log.error("Falha ao recuperar token admin do Keycloak: {}", err.getMessage()));
    }
}
