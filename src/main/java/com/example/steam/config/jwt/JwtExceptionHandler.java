package com.example.steam.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private void setErrorResponse(HttpStatus status, HttpServletResponse response, String message) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(status.value());

        Map<String, Object> body = new HashMap<>();
        body.put("resultCode", "FAIL");
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    // 인증 실패 처리
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        setErrorResponse(HttpStatus.UNAUTHORIZED, response, "유효하지 않은 토큰입니다.");
    }

    // 인가 실패 처리
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        setErrorResponse(HttpStatus.FORBIDDEN, response, "접근 권한이 없습니다.");
    }
}
