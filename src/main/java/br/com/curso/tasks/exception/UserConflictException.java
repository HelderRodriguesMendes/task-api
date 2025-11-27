package br.com.curso.tasks.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserConflictException extends RuntimeException {
    private final HttpStatus status;

    public UserConflictException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}