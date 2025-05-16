package com.example.steam.domain.auth;

import com.example.steam.domain.auth.dto.UserJoinRequest;
import com.example.steam.domain.auth.dto.UserJoinResponse;
import com.example.steam.domain.auth.dto.UserLoginRequest;
import com.example.steam.util.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
@Tag(name = "유저", description = "유저 관련 API (회원가입/로그인)")
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "회원가입 API",
            description = "사용자로부터 아이디, 비밀번호, 이메일을 입력받아 회원가입을 처리합니다."
    )
    @PostMapping("/signup")
    public Response<UserJoinResponse> join(@Valid @RequestBody UserJoinRequest request) {
        UserJoinResponse response = authService.join(request);
        log.info("[LOG 새 회원가입 - userId : {} ", response.getUsername());
        return Response.success(response);
    }

    @Operation(
            summary = "로그인 API",
            description = "아이디와 비밀번호로 로그인하고, 성공 시 Authorization 헤더에 토큰을 반환합니다."
    )
    @PostMapping("/login")
    public Response<Void> login(@Valid @RequestBody UserLoginRequest request, HttpServletResponse response) {
        String accessToken = authService.login(request.getUsername(), request.getPassword());

        response.addHeader("Authorization", accessToken);
        log.info("[로그인] user - {}", request.getUsername());
        return Response.success();
    }

    @Operation(
            summary = "어드민 회원가입 API",
            description = "사용자로부터 아이디, 비밀번호, 이메일을 입력받아 회원가입을 처리합니다."
    )
    @PostMapping("/signup-admin")
    public Response<UserJoinResponse> adminJoin(@Valid @RequestBody UserJoinRequest request) {
        UserJoinResponse response = authService.joinAdmin(request);

        return Response.success(response);
    }

    @Operation(
            summary = "회원 탈퇴",
            description = "회원 탈퇴"
    )
    @DeleteMapping()
    public Response<Void> deleteUser(@RequestParam("user") Long userId) {
        authService.deleteUser(userId);

        return Response.success();
    }
}
