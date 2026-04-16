package br.com.curso.tasks.util;

import br.com.curso.tasks.dto.request.TaskRequestDTO;
import br.com.curso.tasks.dto.request.UserRequestDTO;
import br.com.curso.tasks.dto.response.TaskResponseDTO;
import br.com.curso.tasks.dto.response.UserResponseDTO;
import br.com.curso.tasks.entity.Task;
import br.com.curso.tasks.entity.User;

import java.util.ArrayList;

public class ConvertEntityAndDTO {

    private ConvertEntityAndDTO() {
        throw new IllegalStateException("Utility class");
    }

    public static User convertToUserRequestDTO(UserRequestDTO userRequestDTO) {
        return User.builder()
            .id(userRequestDTO.getId())
            .name(userRequestDTO.getName())
            .email(userRequestDTO.getEmail())
            .phone(userRequestDTO.getPhone())
            .build();

    }

    public static UserResponseDTO convertToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .tasksCreated(user.getTasksCreated() == null ? new ArrayList<>() : user.getTasksCreated().stream().map(ConvertEntityAndDTO::convertToTaskResponseDTO).toList())
            .build();
    }

    public static Task convertToTaskEntity(TaskRequestDTO taskRequestDTO) {

        return Task.builder()
            .id(taskRequestDTO.getId())
            .title(taskRequestDTO.getTitle())
            .description(taskRequestDTO.getDescription())
            .local(taskRequestDTO.getLocal())
            .dateTime(taskRequestDTO.getDateTime())
            .guests(new ArrayList<>())
            .build();

    }

    public static TaskResponseDTO convertToTaskResponseDTO(Task task) {
        return TaskResponseDTO.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .local(task.getLocal())
            .dateTime(task.getDateTime())
            .created(task.getCreated())
            .build();
    }
}