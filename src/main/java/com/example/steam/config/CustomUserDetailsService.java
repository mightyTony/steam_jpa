package com.example.steam.config;

import com.example.steam.domain.auth.AuthRepository;
import com.example.steam.domain.user.User;
import com.example.steam.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
// FIXME : 로그 찍기용 나중엔 @Slf4j 지우자
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        log.info("[최초 로그인 CustomUserDetailsService] 시작");
        User user = authRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        log.info("[로그인] - user : {}", user.getUsername());
        return user;
    }
}
