package br.com.curso.tasks.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private List<TaskResponseDTO> tasksCreated;
}