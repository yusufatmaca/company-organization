package com.deltasmarttech.companyorganization.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "403 Forbidden");
        errorResponse.put("message", "You do not have permission to access this resource.");

        String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);
    }

}