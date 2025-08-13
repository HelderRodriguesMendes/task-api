package br.com.curso.tasks.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotFound extends RuntimeException {
    private String messageError;
    private ExceptionResponseDTO exceptionResponseDTO;
}