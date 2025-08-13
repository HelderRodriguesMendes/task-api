package br.com.curso.tasks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Titulo é obrigatório")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Descrição é obrigatório")
    private String description;

    @NotBlank(message = "Local é obrigatório")
    @Column(nullable = false)
    private String local;

    @Column(nullable = false)
    private LocalDateTime dateTime;
}