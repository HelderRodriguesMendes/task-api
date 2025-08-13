package br.com.curso.tasks.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageException {
    TASK_NOT_FOUND ("Tarefa não encontrada");

    private final String message;
}