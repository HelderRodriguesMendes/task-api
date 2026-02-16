package br.com.curso.tasks.exception;

import org.springframework.lang.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import br.com.curso.tasks.enums.MessageException;

@ControllerAdvice
public class TaskExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(HttpStatus.BAD_REQUEST, errors);
        logger.info("REQUIRED FIELDS: " + responseDTO.getErrors());
        return handleExceptionInternal(ex, responseDTO, headers, status, request);
    }

    @ExceptionHandler(NotFound.class)
    protected ResponseEntity<Object> handleNotFound(NotFound ex, WebRequest request) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(
            ex.getStatus(),
            ex.getMessage()
        );

        return handleExceptionInternal(
            ex,
            responseDTO,
            new HttpHeaders(),
            HttpStatus.valueOf(responseDTO.getStatusCode()),
            request
        );
    }

    @ExceptionHandler(UserConflictException.class)
    protected ResponseEntity<Object> handleUserConflict(UserConflictException ex, WebRequest request) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(
            ex.getStatus(),
            ex.getMessage()
        );

        return handleExceptionInternal(
            ex,
            responseDTO,
            new HttpHeaders(),
            HttpStatus.valueOf(responseDTO.getStatusCode()),
            request
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(
            HttpStatus.FORBIDDEN,
            MessageException.FORBIDDEN_ACCESS.getMessage()
        );

        return handleExceptionInternal(
            ex,
            responseDTO,
            new HttpHeaders(),
            HttpStatus.FORBIDDEN,
            request
        );
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Ocorreu um erro inesperado: " + ex.getMessage()
        );

        return handleExceptionInternal(
            ex,
            responseDTO,
            new HttpHeaders(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            request
        );
    }
}
