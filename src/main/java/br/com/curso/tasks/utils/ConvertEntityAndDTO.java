package br.com.curso.tasks.utils;

import br.com.curso.tasks.dto.request.TaskRequestDTO;
import br.com.curso.tasks.dto.request.UserRequestDTO;
import br.com.curso.tasks.dto.response.TaskResponseDTO;
import br.com.curso.tasks.dto.response.UserResponseDTO;
import br.com.curso.tasks.entity.Task;
import br.com.curso.tasks.entity.User;

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
            .build();
    }

    public static Task convertToTaskEntity(TaskRequestDTO taskRequestDTO) {
        User user  = User.builder()
            .id(taskRequestDTO.getCreated().getId())
            .name(taskRequestDTO.getCreated().getName())
            .email(taskRequestDTO.getCreated().getEmail())
            .phone(taskRequestDTO.getCreated().getPhone())
            .build();

        return Task.builder()
            .id(taskRequestDTO.getId())
            .title(taskRequestDTO.getTitle())
            .description(taskRequestDTO.getDescription())
            .local(taskRequestDTO.getLocal())
            .dateTime(taskRequestDTO.getDateTime())
            .createdId(user)
            .build();
    }

    public static TaskResponseDTO convertToTaskResponseDTO(Task task) {
        return TaskResponseDTO.builder()
            .id(task.getId())
            .title(task.getTitle())
            .description(task.getDescription())
            .local(task.getLocal())
            .dateTime(task.getDateTime())
            .createdId(task.getCreatedId())
            .guests(task.getGuests())
            .build();
    }
}