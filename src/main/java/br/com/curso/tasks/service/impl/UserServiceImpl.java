package br.com.curso.tasks.service.impl;

import br.com.curso.tasks.dto.request.GuestRequestDTO;
import br.com.curso.tasks.entity.PendingGuest;
import br.com.curso.tasks.enums.MessageException;
import br.com.curso.tasks.exception.NotFound;
import br.com.curso.tasks.entity.User;
import br.com.curso.tasks.exception.UserConflictException;
import br.com.curso.tasks.repository.PendingGuestRepository;
import br.com.curso.tasks.repository.UserRepository;
import br.com.curso.tasks.service.KeycloakUserClientService;
import br.com.curso.tasks.service.contract.UserService;
import br.com.curso.tasks.service.requestclient.KeycloakUserRequest;
import br.com.curso.tasks.service.requestclient.ResetPasswordRequest;
import br.com.curso.tasks.util.PasswordGeneratorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final KeycloakUserClientService keycloakUserClientService;

    private final PendingGuestRepository pendingGuestRepository;

    @Override
    public User save(User user) {
        log.info("Saving user with id {}", user.getId());
        Optional<User> existing = userRepository.findByEmail(user.getEmail());
        if (existing.isPresent()) {
            log.info("User with email {} already exists", user.getEmail());
            throw new UserConflictException(MessageException.EMAIL_REGISTERED.getMessage(), HttpStatus.CONFLICT);
        }
        User userSaved = userRepository.save(user);
        log.info("User saved with id {}", userSaved.getId());
        return userSaved;
    }

    @Override
    public User findById(Long id) {
        log.info("Finding user with id {}", id);
        Optional<User> optionalUser = userRepository.findWithTasksCreatedById(id);
        if (optionalUser.isEmpty()){
            log.info("User not found with id {}", id);
           throw new NotFound(MessageException.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }
        log.info("User found with id {}", id);
        return optionalUser.get();
    }

    @Override
    public List<User> findAll() {
        log.info("Finding all users");
        List<User> list = userRepository.findAll();
        if(list.isEmpty()){
            log.info("No users found");
            throw new NotFound(MessageException.LIST_USERS_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }
        log.info("Users found: {}", list.size());
        return list;
    }

    @Override
    public List<User> getUserActive(List<GuestRequestDTO> emails) {
        log.info("Finding users by emails");
        List<User> users = emails.stream()
            .map(GuestRequestDTO::getEmail)
            .map(userRepository::findByEmail)
            .flatMap(Optional::stream)
            .toList();
        log.info("Users found by emails: {}", users.size());
        return users;
    }

    @Override
    public User findByEmail(String email, boolean validateEmailExists) {
        log.info("Finding user by email {}", email);
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(validateEmailExists && optionalUser.isPresent()){
            log.info("User with email {} already exists", email);
            throw new UserConflictException(MessageException.EMAIL_REGISTERED.getMessage(), HttpStatus.CONFLICT);
        }

        if (optionalUser.isEmpty()) {
            log.info("User not found by email {}", email);
            throw new NotFound(MessageException.USER_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }
        log.info("User found by email {}", email);
        return optionalUser.get();
    }

    @Transactional
    @Override
    public User update(Long id, User user) {
        log.info("Updating user with id {}", id);
        User userSave = userRepository.findById(id).get();
        if (!userSave.getEmail().equals(user.getEmail())){
            findByEmail(user.getEmail(), true);
        }
        User updateUser = findById(id);
        updateUser.setName(user.getName());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        User updatedUser = userRepository.save(updateUser);
        log.info("User updated with id {}", id);
        return updatedUser;
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting user with id {}", id);
        findById(id);
        userRepository.deleteById(id);
        log.info("User deleted with id {}", id);
    }

    @Transactional
    @Override
    public Mono<Integer> keycloakCreateUser(PendingGuest pendingGuest) {
        log.info("Creating user in Keycloak for email {}", pendingGuest.getGuestEmail());
        KeycloakUserRequest keycloakUserRequest = getKeycloakUserRequest(pendingGuest);
        String password = PasswordGeneratorUtil.generate(10);
        log.info("Generated password for user {}: {}", pendingGuest.getGuestEmail(), password);

        ResetPasswordRequest passwordRequest = new ResetPasswordRequest("password", password, true);

        return keycloakUserClientService.createUser(keycloakUserRequest)
            .flatMap(userId -> keycloakUserClientService.resetPassword(userId, passwordRequest)
                .then(Mono.fromCallable(() -> {
                    int updated = pendingGuestRepository.updateKeycloakIdById(userId, pendingGuest.getId());
                    log.info("PendingGuest updated (id={}, email={}): keycloakId={}", pendingGuest.getId(), pendingGuest.getGuestEmail(), userId);
                    return updated;
                }))
            )
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(1)).filter(throwable -> {
                String cls = throwable.getClass().getSimpleName();
                return cls.contains("WebClientResponseException") || cls.contains("Timeout") || cls.contains("IOException");
            }))
            ;
    }

    @Override
    public List<PendingGuest> getPendingGuestKeycloak(List<PendingGuest> pendingGuests){
        log.info("Finding pending guests in Keycloak");
        return pendingGuests.stream()
            .map(pendingGuest -> pendingGuestRepository.findByGuestEmailAndKeycloakIdIsNotNull(
                pendingGuest.getGuestEmail()))
            .flatMap(List::stream)
            .toList();
    }

    private KeycloakUserRequest getKeycloakUserRequest(PendingGuest pendingGuest) {
        return KeycloakUserRequest.builder()
            .username(pendingGuest.getGuestEmail())
            .enabled(true)
            .emailVerified(true)
            .email(pendingGuest.getGuestEmail())
            .firstName(pendingGuest.getGuestName())
            .firstName(pendingGuest.getGuestName().split(" ")[0])
            .lastName(pendingGuest.getGuestName().split(" ")[1])
            .requiredActions(List.of("UPDATE_PASSWORD"))
            .build();
    }
}