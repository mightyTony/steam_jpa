package com.example.steam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SteamException extends RuntimeException{
    private final ErrorCode errorCode;
    private String message;

    public SteamException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public SteamException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
