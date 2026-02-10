package br.com.curso.tasks.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageException {
    TASK_NOT_FOUND ("Tarefa não encontrada: "),
    LIST_TASKS_NOT_FOUND ("Nenhuma tarefa encontrada."),
    LIST_USERS_NOT_FOUND ("Nenhum usuário encontrado."),
    USER_NOT_FOUND ("Usuário não encontrado: "),
    GUEST_NOT_FOUND ("Convidado não encontrado: "),
    UNAUTHORIZED_ACCESS ("Token inválido, ausente ou expirado."),
    FORBIDDEN_ACCESS ("Você não tem permissão para acessar esse recurso."),
    TASKS_NOT_FOUND_FOR_USER("Usuário não possui tarefas cadastradas: "),
    EMAIL_REGISTERED ("E-mail já cadastrado: ");

    private final String message;
}