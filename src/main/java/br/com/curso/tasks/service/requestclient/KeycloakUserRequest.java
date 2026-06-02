package br.com.curso.tasks.service.requestclient;

import br.com.curso.tasks.entity.PendingGuest;
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
    private Long id;
    private String username;
    private Boolean enabled;
    private Boolean emailVerified;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> requiredActions;

    public static KeycloakUserRequest getKeycloakUserRequest(PendingGuest pendingGuest) {
        String[] nameParts = pendingGuest.getGuestName().split(" ");
        return KeycloakUserRequest.builder()
            .id(pendingGuest.getId())
            .username(pendingGuest.getGuestEmail())
            .enabled(true)
            .emailVerified(false)
            .email(pendingGuest.getGuestEmail())
            .firstName(nameParts[0])
            .lastName(nameParts.length > 1 ? nameParts[1] : "")
            .requiredActions(List.of("VERIFY_EMAIL", "UPDATE_PASSWORD"))
            .build();
    }
}