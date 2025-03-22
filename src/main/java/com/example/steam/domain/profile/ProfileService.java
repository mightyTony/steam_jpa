package com.example.steam.domain.profile;

import com.example.steam.domain.profile.dto.ProfileResponse;
import com.example.steam.domain.profile.query.ProfileRepository;
import com.example.steam.domain.user.User;
import com.example.steam.domain.user.UserRepository;
import com.example.steam.domain.user.UserService;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    @Transactional(readOnly = true)
    public ProfileResponse getProfileInfo(Long userId) {
        // 유저 검증
        userService.isExistedBoolean(userId);
        log.info("[프로필 정보 쿼리]");
        // 프로필 정보
        // fixme N+1 발생
        return profileRepository.findByUserId(userId);


    }
}
