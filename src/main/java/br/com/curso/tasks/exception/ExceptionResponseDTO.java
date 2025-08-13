package br.com.curso.tasks.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionResponseDTO {
    private HttpStatus httpStatus;
    private Object body;

    public ExceptionResponseDTO(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}