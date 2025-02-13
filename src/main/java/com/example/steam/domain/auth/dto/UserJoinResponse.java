package com.example.steam.domain.auth.dto;

import com.example.steam.domain.user.Role;
import com.example.steam.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserJoinResponse {

    private Long id;
    private String username;
    private Role role;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
