package br.com.curso.tasks.controller;

import br.com.curso.tasks.dto.request.UserRequestDTO;
import br.com.curso.tasks.dto.response.UserResponseDTO;
import br.com.curso.tasks.entity.User;
import br.com.curso.tasks.service.UserService;
import br.com.curso.tasks.utils.ConvertEntityAndDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        User userSaved = userService.save(ConvertEntityAndDTO.convertToUserRequestDTO(userRequestDTO));
        log.info("User saved: {}", userSaved);
        return new ResponseEntity<>(ConvertEntityAndDTO.convertToUserResponseDTO(userSaved), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        log.info("User found by id {}: {}", id, user);
        return ResponseEntity.ok(ConvertEntityAndDTO.convertToUserResponseDTO(user));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<User> users = userService.findAll();
        log.info("Users found: {}", users.size());

        return ResponseEntity.ok(
            users.stream()
                .map(ConvertEntityAndDTO::convertToUserResponseDTO)
                .toList()
        );
    }

//    @GetMapping("/name/{name}")
//    public ResponseEntity<User> findByName(@PathVariable String name) {
//        User user = userService.findByTitle(name); // no service está como findByTitle, mas deveria ser findByName
//        log.info("User found by name {}: {}", name, user);
//        return ResponseEntity.ok(user);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UserRequestDTO userRequestDTO) {
        User updatedUser = userService.update(id, ConvertEntityAndDTO.convertToUserRequestDTO(userRequestDTO));
        log.info("User updated: {}", updatedUser);
        return ResponseEntity.ok(ConvertEntityAndDTO.convertToUserResponseDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        log.info("User deleted with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
