package br.com.curso.tasks.exception;

import br.com.curso.tasks.enums.MessageException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        ExceptionResponseDTO exceptionResponseDTO = ExceptionResponseDTO.builder()
            .statusCode(HttpServletResponse.SC_UNAUTHORIZED)
            .status("UNAUTHORIZED")
            .errors(MessageException.UNAUTHORIZED_ACCESS.getMessage())
            .build();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponseDTO));
    }
}