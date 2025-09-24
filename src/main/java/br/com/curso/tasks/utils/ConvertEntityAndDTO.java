package br.com.curso.tasks.utils;

import br.com.curso.tasks.dto.TaskRequestDTO;
import br.com.curso.tasks.dto.UserRequestDTO;
import br.com.curso.tasks.entity.Task;
import br.com.curso.tasks.entity.User;

import java.time.LocalDateTime;

public class ConvertEntityAndDTO {

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
            .dateTime(LocalDateTime.now())
            .build();
    }

    public static User convertToUserRequestDTO(UserRequestDTO userRequestDTO) {
        return User.builder()
            .id(userRequestDTO.getId())
            .name(userRequestDTO.getName())
            .email(userRequestDTO.getEmail())
            .phone(userRequestDTO.getPhone())
            .build();

    }
}