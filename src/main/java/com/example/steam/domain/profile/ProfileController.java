package com.example.steam.domain.profile;

import com.example.steam.domain.profile.dto.CommentPostRequest;
import com.example.steam.domain.profile.dto.CommentResponse;
import com.example.steam.domain.profile.dto.ProfileResponse;
import com.example.steam.domain.profile.dto.ProfileUpdateRequest;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    @PatchMapping(value = "/user/{userId}/edit/profile/info")
    @LoginUser
    public Response<ProfileResponse> updateProfileInfo(@AuthenticationPrincipal User user,
                                                       @PathVariable("userId") Long userId,
                                                       @RequestBody ProfileUpdateRequest request) {
        ProfileResponse response = profileService.editProfileInfo(user, userId, request);

        return Response.success(response);
    }

    /*
        # 프로필 사진 변경
        - 요청 : 파일, 유저 아이디
        - 응답 : 이미지 경로
     */
    @PutMapping("/user/{userId}/edit/profile/image")
    @LoginUser
    public Response<String> editProfileImage(
                                        @AuthenticationPrincipal User user,
                                        @RequestPart("image")MultipartFile imageFile,
                                        @PathVariable("userId") Long userId) throws IOException {
        String imageUrl = profileService.editProfileImage(imageFile, userId);

        return Response.success(imageUrl);
    }

    /*
        프로필 댓글 작성
        POST
        req : content, user, profileId
        res : content, writerId, writerName, createdTime, updatedTime
     */
    @PostMapping("/{profileId}/comment")
    @LoginUser
    public Response<CommentResponse> addComment(@AuthenticationPrincipal User writer,
                                                @PathVariable("profileId") Long profileId,
                                                @Valid @RequestBody CommentPostRequest request) {
        log.info("[프로필 댓글 작성] 시작 ");
        CommentResponse response = profileService.postComment(writer, profileId, request);
        log.info("[프로필 댓글 작성] 끝");
        return Response.success(response);
    }



    /*
        프로필 댓글 페이징 조회
        GET
        req : userId
        res : Page<CommentResponse> , userId,content, writerId, writerName, createTime, updateTime
     */

    /*
        프로필 댓글 삭제
        DELETE
        req : commentId
     */
}
