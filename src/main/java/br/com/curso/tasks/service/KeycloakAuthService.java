package br.com.curso.tasks.service;

import br.com.curso.tasks.service.response.KeycloakTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class KeycloakAuthService {

    private final WebClient webClient;

    @Value("${keykloak.client.id}")
    private String clientIdKeycloak;

    @Value("${keykloak.client.username}")
    private String usernameKeycloak;

    @Value("${keykloak.client.password}")
    private String passwordKeycloak;

    @Value("${keykloak.client.grant-type}")
    private String grantTypeKeycloak;

    public KeycloakAuthService(String keycloakEndpoint) {
        this.webClient = WebClient.builder()
            .baseUrl(keycloakEndpoint)
            .defaultHeader("Content-Type", "application/x-www-form-urlencoded")
            .build();
    }

    public Mono<String> getAccessTokenAdmin(){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientIdKeycloak);
        formData.add("username", usernameKeycloak);
        formData.add("password", passwordKeycloak);
        formData.add("grant_type", grantTypeKeycloak);

        return webClient.post()
            .uri("/realms/schedule-task/protocol/openid-connect/token")
            .bodyValue(formData)
            .retrieve()
            .bodyToMono(KeycloakTokenResponse.class)
            .map(KeycloakTokenResponse::getAccessToken);
    }
}