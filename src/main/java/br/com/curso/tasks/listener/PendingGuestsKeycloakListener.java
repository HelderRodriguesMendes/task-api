package br.com.curso.tasks.listener;

import br.com.curso.tasks.event.PendingGuestsCreatedEvent;
import br.com.curso.tasks.entity.PendingGuest;
import br.com.curso.tasks.enums.PendingGuestStatus;
import br.com.curso.tasks.repository.PendingGuestRepository;
import br.com.curso.tasks.service.contract.UserService;
import br.com.curso.tasks.service.requestclient.KeycloakUserRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import reactor.core.scheduler.Schedulers;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class PendingGuestsKeycloakListener {

    private final PendingGuestRepository pendingGuestRepository;
    private final UserService userService;

    @Async("keycloakExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePendingGuestsCreated(PendingGuestsCreatedEvent event) {
        log.info("Handling PendingGuestsCreatedEvent for task {}. Processing {} pending ids on thread {}",
            event.getTaskId(), event.getPendingGuestIds().size(), Thread.currentThread().getName());

        for (Long id : event.getPendingGuestIds()) {
            log.debug("Queued pendingGuest id={} for processing", id);
            processPendingGuest(id);
        }
    }

    private void processPendingGuest(Long id) {
        try {
            log.debug("Processing pendingGuest id={}", id);
            Optional<PendingGuest> opt = pendingGuestRepository.findById(id);
            if (opt.isEmpty()) {
                log.warn("PendingGuest id={} not found when processing Keycloak", id);
                return;
            }

            PendingGuest pendingGuest = opt.get();
            int s = pendingGuestRepository.updateStatus(id, PendingGuestStatus.PROCESSING.name());
            int a = pendingGuestRepository.incrementAttempts(id);
            log.debug("Updated status rows={}, incrementAttempts rows={} for pendingGuest id={}", s, a, id);

            createKeycloakUser(id, pendingGuest);

        } catch (Exception ex) {
            log.error("Exception processing pendingGuest id={}: {}", id, ex.getMessage(), ex);
        }
    }

    private void createKeycloakUser(Long id, PendingGuest pendingGuest) {
        KeycloakUserRequest keycloakUserRequest = KeycloakUserRequest.getKeycloakUserRequest(pendingGuest);
        userService.keycloakCreateUser(keycloakUserRequest)
            .subscribeOn(Schedulers.boundedElastic())
            .subscribe(
                updated -> {
                    log.info("Keycloak user created for {} and updated {} records", pendingGuest.getGuestEmail(), updated);
                    pendingGuestRepository.updateStatus(id, PendingGuestStatus.KEYCLOAK_CREATED.name());
                },
                error -> {
                    log.error("Failed to create Keycloak user for {}: {}", pendingGuest.getGuestEmail(), error.getMessage());
                    pendingGuestRepository.updateStatus(id, PendingGuestStatus.FAILED.name());
                }
            );
    }
}