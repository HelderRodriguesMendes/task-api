package br.com.curso.tasks.entity;

import br.com.curso.tasks.enums.PendingGuestStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pending_guest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingGuest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @NonNull
    @Column(name = "guest_name", nullable = false)
    private String guestName;

    @NonNull
    @Column(name = "guest_email", nullable = false)
    private String guestEmail;

    @Column(name = "keycloak_id")
    private String keycloakId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private PendingGuestStatus status = PendingGuestStatus.PENDING;

    @Column(name = "attempts", nullable = false)
    @Builder.Default
    private Integer attempts = 0;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}