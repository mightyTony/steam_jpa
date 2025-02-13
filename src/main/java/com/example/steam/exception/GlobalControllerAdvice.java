package com.example.steam.exception;

import com.example.steam.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    @ExceptionHandler(SteamException.class)
    public ResponseEntity<?> applicationHandler(SteamException e) {
        log.info("Exception occurs CODE = {}, msg = {}", e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().name(), e.getErrorCode().getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception e) {
        log.error("ERROR OCCURS {} ", "\n" + e.toString());
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }
}
