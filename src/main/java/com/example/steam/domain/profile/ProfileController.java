package com.example.steam.domain.profile;

import com.example.steam.domain.game.dto.GameResponse;
import com.example.steam.domain.profile.dto.*;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
        CommentResponse response = profileService.postComment(writer, profileId, request);
        return Response.success(response);
    }


    /*
        프로필 댓글 삭제
        DELETE
        req : commentId
     */
    @DeleteMapping("/{profileId}/comment/{commentId}")
    @LoginUser
    public Response<Void> deleteComment(@AuthenticationPrincipal User user,
                                        @PathVariable("profileId") Long profileId,
                                        @PathVariable("commentId") Long commentId) {

        profileService.deleteComment(user, profileId, commentId);

        return Response.success();
    }


    /*
        프로필 댓글 페이징 조회
        GET
        req : userId, offset, size
        res : Page<CommentResponse> , userId,content, writerId, writerName, createTime, updateTime
     */
    @GetMapping("/{profileId}/comment")
    public Response<Page<CommentResponse>> getComments(@PathVariable("profileId") Long profileId,
                                                       @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                       @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                       Pageable pageable) {

        Page<CommentResponse> responses = profileService.getComments(profileId, page, size, pageable);

        return Response.success(responses);
    }

    // 보유 게임 조회
    @GetMapping("/user/{userId}/game")
    public Response<List<MyGameResponse>> getMyGames(@PathVariable("userId") Long userId) {

        List<MyGameResponse> result = profileService.getMyGames(userId);

        return Response.success(result);
    }

}
