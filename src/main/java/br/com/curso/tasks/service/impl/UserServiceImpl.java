package br.com.curso.tasks.service.impl;

import br.com.curso.tasks.enums.MessageException;
import br.com.curso.tasks.exception.NotFound;
import br.com.curso.tasks.entity.User;
import br.com.curso.tasks.exception.UserConflictException;
import br.com.curso.tasks.repository.UserRepository;
import br.com.curso.tasks.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        validateEmailNotExists(user.getEmail());
        User userSaved = userRepository.save(user);
        log.info("User saved with id {}", userSaved.getId());
        return userSaved;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFound(
            MessageException.USER_NOT_FOUND.getMessage(),
            HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findByEmail(List<String> emails) {
        List<User> users = emails.stream()
            .map(userRepository::findByEmail)
            .flatMap(Optional::stream)
            .toList();
        return users;
    }

    @Transactional
    @Override
    public User update(Long id, User user) {
        validateEmailNotExists(user.getEmail());
        User updateUser = findById(id);
        updateUser.setName(user.getName());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        return updateUser;
    }

    @Override
    public void delete(Long id) {
        findById(id);
        userRepository.deleteById(id);
    }

    @Override
    public void validateEmailNotExists(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            throw new UserConflictException(MessageException.EMAIL_REGISTERED.getMessage(), HttpStatus.CONFLICT);
        }
    }
}