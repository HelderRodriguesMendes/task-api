package br.com.curso.tasks.service.requestClient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
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