package com.example.steam.exception;

import com.example.steam.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(SteamException.class)
    public ResponseEntity<?> applicationHandler(SteamException e) {
        log.info("[LOG] Exception occurs CODE = {}, msg = {}", e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response.error(e.getErrorCode().name(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception e) {
        log.error("[LOG] ERROR OCCURS {} ", "\n" + e.toString());
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
