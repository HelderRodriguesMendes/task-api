package br.com.curso.tasks.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageException {
    TASK_NOT_FOUND ("Tarefa não encontrada"),
    USER_NOT_FOUND ("Usuário não encontrado"),
    GUEST_NOT_FOUND ("Convidado não encontrado"),
    EMAIL_REGISTERED ("E-mail já cadastrado");

    private final String message;
}