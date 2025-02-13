package com.example.steam.domain.auth;

import com.example.steam.config.jwt.JwtTokenProvider;
import com.example.steam.domain.auth.dto.UserJoinRequest;
import com.example.steam.domain.auth.dto.UserJoinResponse;
import com.example.steam.domain.user.Role;
import com.example.steam.domain.user.User;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    @Transactional
    public UserJoinResponse join(UserJoinRequest request) {
        // 이미 가입된 아이디인지
        if(authRepository.findByUsername(request.getUsername()).isPresent()) {
           throw new SteamException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s 는 중복된 이름 입니다.", request.getUsername()));
        }

        // 회원가입 진행
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        authRepository.save(user);

        return UserJoinResponse.fromUser(user);
    }

    public String login(String username, String password) {
        // 회원가입 여부 체크
        User user = authRepository.findByUsername(username)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_USER_NAME, ErrorCode.NOT_FOUND_USER_NAME.getMessage()));

        // 비밀번호 체크
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new SteamException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());

        return token;
    }
}
