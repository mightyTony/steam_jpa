package com.example.steam.domain.auth.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class UserLoginRequest {
    private String username;
    private String password;
}
