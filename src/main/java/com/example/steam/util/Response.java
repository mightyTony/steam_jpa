package com.example.steam.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T>{
    private String resultCode;
    private T result;

    public Response(String resultCode) {
        this.resultCode = resultCode;
    }

    public static <T> Response<T> error(String errorCode, String message) {
        return (Response<T>) new Response<>(errorCode, message);
    }

    public static <T> Response<T> success(T result) {
        return new Response<>("SUCCESS", result);
    }

    public static <T> Response<T> success() {
        return new Response<>("SUCCESS");
    }
}
