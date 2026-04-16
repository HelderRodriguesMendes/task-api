package br.com.curso.tasks.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            Map<String, Object> claims = new HashMap<>();
            claims.put("sub", "test-user");
            claims.put("preferred_username", "testuser");
            claims.put("scope", "TASK");
            Instant now = Instant.now();
            return new Jwt(token, now, now.plusSeconds(3600), Map.of("alg", "none"), claims);
        };
    }
}