package br.com.curso.tasks.dto.response;

import br.com.curso.tasks.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String local;
    private LocalDateTime dateTime;
    private User created;
}