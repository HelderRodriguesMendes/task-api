package br.com.curso.tasks.service.requestclient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakUserRequest {
    private String username;
    private Boolean enabled;
    private Boolean emailVerified;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> requiredActions;
}