package br.com.curso.tasks.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
}