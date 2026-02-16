package br.com.curso.tasks.service;

import br.com.curso.tasks.service.responseClient.KeycloakTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KeycloakAuthService {

    private final WebClient webClient;

    @Value("${spring.keykloak.id}")
    private String clientIdKeycloak;

    @Value("${spring.keykloak.username}")
    private String usernameKeycloak;

    @Value("${spring.keykloak.password}")
    private String passwordKeycloak;

    @Value("${spring.keykloak.grant-type}")
    private String grantTypeKeycloak;

    @Value("${spring.keykloak.uri-access-token-admin}")
    private String uriAcessTokenAdmin;

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
            .uri(uriAcessTokenAdmin)
            .bodyValue(formData)
            .retrieve()
            .bodyToMono(KeycloakTokenResponse.class)
            .map(KeycloakTokenResponse::getAccessToken);
    }
}