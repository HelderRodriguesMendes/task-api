package br.com.curso.tasks.repository;

import br.com.curso.tasks.entity.PendingGuest;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendingGuestRepository extends JpaRepository<PendingGuest, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE pending_guest pg SET pg.keycloak_id = :keycloakId WHERE pg.id = :id AND pg.keycloak_id IS NULL", nativeQuery = true)
    int updateKeycloakIdById(@Param("keycloakId") String keycloakId, @Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE pending_guest pg SET pg.status = :status WHERE pg.id = :id", nativeQuery = true)
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = "UPDATE pending_guest pg SET pg.attempts = pg.attempts + 1 WHERE pg.id = :id", nativeQuery = true)
    int incrementAttempts(@Param("id") Long id);

    List<PendingGuest> findByKeycloakId(String keycloakId);

    List<PendingGuest> findByGuestEmailAndKeycloakIdIsNotNull(String email);
}