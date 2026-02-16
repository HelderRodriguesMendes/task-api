package br.com.curso.tasks.controller;

import br.com.curso.tasks.dto.request.TaskRequestDTO;
import br.com.curso.tasks.dto.response.TaskResponseDTO;
import br.com.curso.tasks.entity.Task;
import br.com.curso.tasks.service.contract.TaskService;
import br.com.curso.tasks.utils.ConvertEntityAndDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @PreAuthorize("hasRole('TASK')")
    @PostMapping
    public ResponseEntity<TaskResponseDTO> save(@RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        Task taskSaved = taskService.save(taskRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ConvertEntityAndDTO.convertToTaskResponseDTO(taskSaved));
    }

    @PreAuthorize("hasRole('TASK')")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findById(@PathVariable Long id) {
        Task task = taskService.findById(id);
        return ResponseEntity.ok(ConvertEntityAndDTO.convertToTaskResponseDTO(task));
    }

    @PreAuthorize("hasRole('TASK')")
    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> findAll() {
        List<Task> tasks = taskService.findAll();
        return ResponseEntity.ok(
            tasks.stream().map(ConvertEntityAndDTO::convertToTaskResponseDTO)
                .toList()
        );
    }

    @PreAuthorize("hasRole('TASK')")
    @GetMapping("/title/{title}")
    public ResponseEntity<TaskResponseDTO> findByTitle(@PathVariable String title) {
        Task task = taskService.findByTitle(title);
        return ResponseEntity.ok(ConvertEntityAndDTO.convertToTaskResponseDTO(task));
    }

    @PreAuthorize("hasRole('TASK')")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable Long id, @RequestBody @Valid TaskRequestDTO taskRequestDTO) {
        Task updatedTask = taskService.update(id, taskRequestDTO);
        return ResponseEntity.ok(ConvertEntityAndDTO.convertToTaskResponseDTO(updatedTask));
    }

    @PreAuthorize("hasRole('TASK')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('TASK')")
    @GetMapping("/{userId}/user-tasks")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByUserId(@PathVariable Long userId) {
        List<Task> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(
            tasks.stream().map(ConvertEntityAndDTO::convertToTaskResponseDTO).toList()
        );
    }
}