package br.com.curso.tasks.service.impl;

import br.com.curso.tasks.dto.request.TaskRequestDTO;
import br.com.curso.tasks.entity.Guest;
import br.com.curso.tasks.entity.PendingGuest;
import br.com.curso.tasks.entity.User;
import br.com.curso.tasks.enums.MessageException;
import br.com.curso.tasks.exception.NotFound;
import br.com.curso.tasks.entity.Task;
import br.com.curso.tasks.repository.TaskRepository;
import br.com.curso.tasks.service.contract.GuestService;
import br.com.curso.tasks.service.contract.TaskService;
import br.com.curso.tasks.service.contract.UserService;
import br.com.curso.tasks.utils.ConvertEntityAndDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final GuestService guestService;
    private final UserService userService;

    @Override
    @Transactional
    public Task save(TaskRequestDTO taskRequestDTO) {
        log.info("Saving task with title {}", taskRequestDTO.getTitle());
        Task task = ConvertEntityAndDTO.convertToTaskEntity(taskRequestDTO);
        task.setGuests(new ArrayList<>());

        Task taskSaved = taskRepository.save(task);

        if (taskRequestDTO.getGuests() != null && !taskRequestDTO.getGuests().isEmpty()) {
            addGuestsActive(taskRequestDTO, taskSaved);
            addGuestsPending(taskRequestDTO, taskSaved);
            taskSaved = taskRepository.save(taskSaved);
        }

        log.info("Task saved with id {}", taskSaved.getId());
        return taskSaved;
    }

    @Override
    public Task findById(Long id) {
        log.info("Finding task with id {}", id);
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (!taskOptional.isPresent()) {
            log.info("Task not found with id {}", id);
            throw new NotFound(MessageException.TASK_NOT_FOUND.getMessage() + id, HttpStatus.NOT_FOUND);
        }
        log.info("Task found with id {}", id);
        return taskOptional.get();
    }

    @Override
    public List<Task> findAll() {
        log.info("Finding all tasks");
        List<Task> list = taskRepository.findAll();
        if(list.isEmpty()){
            log.info("Tasks not found");
            throw new NotFound(MessageException.LIST_TASKS_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND);
        }
        log.info("Task found");
        return list;
    }

    @Override
    public Task findByTitle(String title) {
        log.info("Finding task with title {}", title);
        Optional<Task> taskOptional = taskRepository.findByTitle(title);
        if (!taskOptional.isPresent()) {
            log.info("Task not found with title {}", title);
            throw new NotFound(MessageException.TASK_NOT_FOUND.getMessage() + title, HttpStatus.NOT_FOUND);
        }
        log.info("Task found with title {}", title);
        return taskOptional.get();
    }

    @Override
    public Task update(Long id, TaskRequestDTO taskRequestDTO) {
        log.info("Updating task with id {}", id);
        taskRequestDTO.setId(id);
        Task taskSavede =  findById(id);
        taskSavede.getGuests().clear();
        addGuestsActive(taskRequestDTO, taskSavede);
        addGuestsPending(taskRequestDTO, taskSavede);
        Task task =  taskRepository.save(taskSavede);
        log.info("Task updated with id {}", id);
        return task;
    }

    @Override
    public void delete(Long id) {
        log.info("Deleting task with id {}", id);
        findById(id);
        taskRepository.deleteById(id);
        log.info("Task deleted with id {}", id);
    }

    @Override
    public List<Task> getTasksByUserId(Long userId) {
        log.info("Getting tasks by user with id {}", userId);
        Optional<List<Task>> listOptional = taskRepository.findByCreatedId_Id(userId);
        if (!listOptional.isPresent() || listOptional.get().isEmpty()) {
            log.info("No tasks found for user id {}", userId);
            throw new NotFound(MessageException.TASKS_NOT_FOUND_FOR_USER.getMessage() + userId, HttpStatus.NOT_FOUND);
        }
        log.info("Tasks found for user id {}", userId);
        return listOptional.get();
    }

    private void  addGuestsActive(TaskRequestDTO taskRequestDTO, Task task) {
        log.info("Adding guests for task with title {}", taskRequestDTO.getTitle());
        List<User> usersGuests = userService.getUserActive(taskRequestDTO.getGuests());

        List<Guest> guests = usersGuests.stream()
            .map(user -> new  Guest(null, task, user))
            .collect(Collectors.toList());
        task.getGuests().addAll(guests);
        log.info("Added guests for task with title {}", taskRequestDTO.getTitle());
    }

    private void  addGuestsPending(TaskRequestDTO taskRequestDTO, Task task) {
        List<String> emailsGuestsExist = task.getGuests().stream()
            .map(guest -> guest.getUser().getEmail())
            .toList();

        List<PendingGuest> pendingGuests = taskRequestDTO.getGuests().stream()
            .filter(dto -> !emailsGuestsExist.contains(dto.getEmail()))
            .map(dto -> new PendingGuest(null ,task, dto.getName(), dto.getEmail()))
            .toList();
        task.setPendingGuests(new ArrayList<>());
        task.getPendingGuests().addAll(pendingGuests);
    }
}