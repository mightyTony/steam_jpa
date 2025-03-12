package com.example.steam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SteamException extends RuntimeException{
    private final ErrorCode errorCode;

    public SteamException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public SteamException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
