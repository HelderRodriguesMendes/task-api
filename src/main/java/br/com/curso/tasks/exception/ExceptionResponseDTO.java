package br.com.curso.tasks.exception;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ExceptionResponseDTO {
    private int statusCode;
    private String status;
    private LocalDateTime timestamp;
    private Object errors;

    public ExceptionResponseDTO(HttpStatus httpStatus, Object errors) {
        this.statusCode = httpStatus.value();
        this.status = httpStatus.name();
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }
}
