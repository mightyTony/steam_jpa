package com.example.steam.exception;

import lombok.Getter;

@Getter
public class BatchJobException extends RuntimeException{
    private final ErrorCode errorCode;

    public BatchJobException(ErrorCode errorCode, String message) {
        super(message)  ;
        this.errorCode = errorCode;
    }

    public BatchJobException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
