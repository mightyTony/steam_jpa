package com.example.steam.domain.profile;

import com.example.steam.domain.profile.dto.ProfileResponse;
import com.example.steam.domain.profile.query.ProfileRepository;
import com.example.steam.domain.user.User;
import com.example.steam.domain.user.UserRepository;
import com.example.steam.domain.user.UserService;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import com.example.steam.infra.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final S3Util s3Util;
    private final String S3_USER_DIRNAME = "image/user";

    @Transactional(readOnly = true)
    public ProfileResponse getProfileInfo(Long userId) {
        // 유저 검증
        userService.isExistedBoolean(userId);
        log.info("[프로필 정보 쿼리]");
        // 프로필 정보
        // fixme N+1 발생
        return profileRepository.findByUserId(userId);


    }

    @Transactional
    public String editProfileImage(MultipartFile imageFile, Long userId) throws IOException {
        // 유저 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_USER_NAME));

        // 파일 검증
        if(imageFile.isEmpty() || Objects.requireNonNull(imageFile.getOriginalFilename()).isEmpty()) {
            throw new SteamException(ErrorCode.ILLEGAL_ARGUMENT_MULTIPARTFILE);
        }
        // 파일 업로드
        String imageCloudFrontUrl = s3Util.upload(imageFile, S3_USER_DIRNAME);

        // 이미지 변경
        user.updateImage(imageCloudFrontUrl);
        userRepository.save(user);

        log.info("[유저 이미지 변경] userId : {}, uploadImageUrl : {}", user.getId(), user.getProfileImageUrl());

        return user.getProfileImageUrl();
    }
}
