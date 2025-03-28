package com.example.steam.domain.auth;

import com.example.steam.domain.auth.dto.UserJoinRequest;
import com.example.steam.domain.auth.dto.UserJoinResponse;
import com.example.steam.domain.auth.dto.UserLoginRequest;
import com.example.steam.domain.auth.dto.UserLoginResponse;
import com.example.steam.util.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        UserJoinResponse response = authService.join(request);

        return Response.success(response);
    }

    @PostMapping("/login")
    public Response<Void> login(@RequestBody UserLoginRequest request, HttpServletResponse response) {
        String accessToken = authService.login(request.getUsername(), request.getPassword());

        response.addHeader("Authorization", accessToken);
        log.info("[로그인] user - {}", request.getUsername());
        return Response.success();
    }
}
