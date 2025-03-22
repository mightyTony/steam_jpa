package com.example.steam.domain.profile;

import com.example.steam.domain.profile.dto.ProfileResponse;
import com.example.steam.util.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    /*
       # 프로필 가져오기
       - 응답 : 프로필 유저 정보
       - 조건 : 프로필 조회 권한(친구,all) // 보류
     */
    @GetMapping("/user")
    public Response<ProfileResponse> getProfileInfo(@RequestParam("id") Long userId) {
        ProfileResponse response = profileService.getProfileInfo(userId);

        return Response.success(response);
    }

//    /*
//        # 프로필 수정
//        - 파라미터 : 닉네임, 프로필 수정, 할 말
//        - 응답 : 프로필 정보
//     */
//    // todo : https://velog.io/@tyjk8997/Springboot-%EC%9D%B4%EB%AF%B8%EC%A7%80%EC%99%80-%EB%8D%B0%EC%9D%B4%ED%84%B0%EB%A5%BC-%EB%8F%99%EC%8B%9C%EC%97%90-%EC%B2%98%EB%A6%AC%ED%95%A0-%EB%95%8C-%EC%83%9D%EA%B8%B4-%EC%9D%B4%EC%8A%88
//    @PatchMapping(value = "/user", consumes = {MediaType.APPLICATION_JSON_VALUE, "multipart/form-data"})
//    public Response<ProfileResponse> updateProfileInfo(@RequestPart(value = "imageFile", required = false)MultipartFile imageFile,
//                                                       @AuthenticationPrincipal User user) {
//
//    }

}
