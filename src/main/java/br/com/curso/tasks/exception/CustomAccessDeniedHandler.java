package br.com.curso.tasks.exception;

import br.com.curso.tasks.enums.MessageException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ExceptionResponseDTO exceptionResponseDTO = ExceptionResponseDTO.builder()
            .statusCode(HttpServletResponse.SC_FORBIDDEN)
            .status("FORBIDDEN")
            .errors(MessageException.FORBIDDEN_ACCESS.getMessage())
            .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponseDTO));
    }
}