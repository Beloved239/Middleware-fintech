package com.middleware.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.middleware.common.dto.MiddleWareResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.info("Request URI: {}", request.getRequestURI());
        log.info("AuthenticationException: {}", authException.getMessage());

        String errorMessage = (String) request.getAttribute("error-message");
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = "Unauthorized access. Please check your credentials.";
        }

        MiddleWareResponse errorResponse =  new MiddleWareResponse();
        errorResponse.setMessage(errorMessage);
        errorResponse.setSuccess(false);
        errorResponse.setTimestamp(LocalDateTime.now());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));


        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }

}
