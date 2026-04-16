package br.com.curso.tasks.service.requestclient;

public record ResetPasswordRequest(
    String type,
    String value,
    boolean temporary
) {}