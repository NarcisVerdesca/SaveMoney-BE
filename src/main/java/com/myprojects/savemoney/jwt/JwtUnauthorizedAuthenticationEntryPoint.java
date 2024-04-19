package com.myprojects.savemoney.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUnauthorizedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    Logger logger = LoggerFactory.getLogger(JwtUnauthorizedAuthenticationEntryPoint.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        int statusCode;
        String errorMessage;

        if (authException.getMessage() != null && authException.getMessage().contains("Access Denied")) {
            statusCode = HttpStatus.FORBIDDEN.value();
            errorMessage = "Access denied!";
        } else {
            statusCode = HttpStatus.UNAUTHORIZED.value();
            errorMessage = "Unauthorized!";
        }

        response.setStatus(statusCode);
        response.setContentType("application/json");
        Map<String, Object> data = new HashMap<>();
        data.put("Status","ERROR");
        data.put("Message",errorMessage);
        data.put("Success",false);
        response.getOutputStream()
                .println(objectMapper.writeValueAsString(data));
    }

}
