package com.example.steam.domain.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    /*
    프로필 가져오기 (프로필 유저 정보, 친구 수, 친구 정보(닉네임,프로필사진) , 마이 게임) / 프로필 조회 권한(친구,all)
     */

    /*
        프로필 수정 (닉네임, 프로필 수정, 할 말)
     */

    /*
        댓글 삭제
     */
}
