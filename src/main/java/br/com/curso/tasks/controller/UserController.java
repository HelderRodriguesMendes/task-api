package br.com.curso.tasks.controller;

import br.com.curso.tasks.dto.request.UserRequestDTO;
import br.com.curso.tasks.dto.response.UserResponseDTO;
import br.com.curso.tasks.entity.User;
import br.com.curso.tasks.service.contract.UserService;
import br.com.curso.tasks.util.ConvertEntityAndDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        User userSaved = userService.save(ConvertEntityAndDTO.convertToUserRequestDTO(userRequestDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(ConvertEntityAndDTO.convertToUserResponseDTO(userSaved));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(ConvertEntityAndDTO.convertToUserResponseDTO(user));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(
            users.stream()
                .map(ConvertEntityAndDTO::convertToUserResponseDTO)
                .toList()
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/get-by-email")
    public ResponseEntity<UserResponseDTO> findByName(@RequestParam String email) {
        User user = userService.findByEmail(email, false);
        return ResponseEntity.ok(ConvertEntityAndDTO.convertToUserResponseDTO(user));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UserRequestDTO userRequestDTO) {
        User updatedUser = userService.update(id, ConvertEntityAndDTO.convertToUserRequestDTO(userRequestDTO));
        return ResponseEntity.ok(ConvertEntityAndDTO.convertToUserResponseDTO(updatedUser));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
