package com.example.littleBank.exceptions;


import com.example.littleBank.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedEntryPoint implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResponse errorResponse = new ErrorResponse("NotAuthorizedException", "ACCESSO NEGATO! NON HAI L'AUTORIZZAZIONE PER ACCEDERE A QUESTA RISORSA");
        String myMessage = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(myMessage);
    }
}
