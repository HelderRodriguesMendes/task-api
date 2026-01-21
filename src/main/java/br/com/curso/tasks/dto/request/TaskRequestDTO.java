package br.com.curso.tasks.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TaskRequestDTO {
    @Setter
    private Long id;

    @NotBlank(message = "Titulo é obrigatório")
    @Size(min = 5, max = 30, message = "O tamanho mínimo de 5 caracteres e máximo de 15 caracteres")
    private String title;

    @NotBlank(message = "Descrição é obrigatório")
    private String description;

    @NotBlank(message = "Local é obrigatório")
    private String local;

    @NotNull(message = "Data e hora são obrigatórios")
    private LocalDateTime dateTime;

    @NotNull
    private UserRequestDTO created;

    @NotNull(message = "Nenhum convidado foi selecionado")
    private List<GuestRequestDTO> guests;
}