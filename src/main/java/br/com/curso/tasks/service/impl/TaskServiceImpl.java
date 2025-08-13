package br.com.curso.tasks.service.impl;

import br.com.curso.tasks.enums.MessageException;
import br.com.curso.tasks.exception.ExceptionResponseDTO;
import br.com.curso.tasks.exception.NotFound;
import br.com.curso.tasks.model.Task;
import br.com.curso.tasks.repository.TaskRepository;
import br.com.curso.tasks.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    @Override
    public Task save(Task task) {
        task.setDateTime(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Override
    public Task findById(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (!taskOptional.isPresent()) {
            throw new NotFound(MessageException.TASK_NOT_FOUND.getMessage(),
                new ExceptionResponseDTO(HttpStatus.NOT_FOUND));
        }
        return taskOptional.get();
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task findByTitle(String title) {
        Optional<Task> taskOptional = taskRepository.findByTitle(title);
        if (!taskOptional.isPresent()) {
            throw new NotFound(MessageException.TASK_NOT_FOUND.getMessage(),
                new ExceptionResponseDTO(HttpStatus.NOT_FOUND));
        }
        return taskOptional.get();
    }

    @Override
    public Task update(Long id, Task task) {
        findById(id);
        task.setId(id);
        return taskRepository.save(task);
    }

    @Override
    public void delete(Long id) {
        findById(id);
        taskRepository.deleteById(id);
    }
}