package com.example.steam.domain.auth.dto;

import lombok.Getter;

@Getter
// todo : validation
public class UserJoinRequest {
    private String username;
    private String password;
    private String email;
}
