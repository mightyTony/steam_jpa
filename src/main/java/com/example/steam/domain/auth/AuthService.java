package com.example.steam.domain.auth;

import com.example.steam.config.jwt.JwtTokenProvider;
import com.example.steam.domain.auth.dto.UserJoinRequest;
import com.example.steam.domain.auth.dto.UserJoinResponse;
import com.example.steam.domain.profile.Profile;
import com.example.steam.domain.profile.query.ProfileRepository;
import com.example.steam.domain.user.Role;
import com.example.steam.domain.user.User;
import com.example.steam.domain.user.UserRepository;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ProfileRepository profileRepository;

    @Transactional
    public UserJoinResponse join(UserJoinRequest request) {
        // 이미 가입된 아이디인지
        if(authRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail()).isPresent()){
            throw new SteamException(ErrorCode.DUPLICATED_USER_NAME_OR_EMAIL);
        }

        // 회원가입 진행
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        authRepository.save(user);

        Profile profile = Profile.builder()
                .user(user)
                .content("")
                .build();
        profileRepository.save(profile);

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

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        // 토큰 생성
        return jwtTokenProvider.createToken(authenticationToken);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_USER_NAME));

        log.info("[회원 탈퇴] - user : {}", user.getUsername());

        userRepository.delete(user);
    }

    @Transactional
    public UserJoinResponse joinAdmin(UserJoinRequest request) {
        // 이미 가입된 아이디인지
        if(authRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail()).isPresent()){
            throw new SteamException(ErrorCode.DUPLICATED_USER_NAME_OR_EMAIL);
        }

        // 회원가입 진행
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();

        authRepository.save(user);

        Profile profile = Profile.builder()
                .user(user)
                .content("")
                .build();
        profileRepository.save(profile);

        return UserJoinResponse.fromUser(user);
    }
}
