package com.example.steam.domain.profile;

import com.example.steam.domain.profile.dto.*;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "프로필", description = "프로필, 댓글, 마이게임 API 입니다")
public class ProfileController {
    private final ProfileService profileService;

    /*
       # 프로필 가져오기
       - 응답 : 프로필 유저 정보
       - 조건 : 프로필 조회 권한(친구,all) // 보류
     */
    @Operation(summary = "프로필 조회", description = "사용자 ID로 프로필 정보를 조회합니다.")
    @LoginUser
    @GetMapping("/user/{userId}")
    public Response<ProfileResponse> getProfileInfo(
            @AuthenticationPrincipal User user,
            @Parameter(description = "조회할 유저 ID", example = "1")
            @PathVariable("userId") Long userId) {
        ProfileResponse response = profileService.getProfileInfo(user, userId);

        return Response.success(response);
    }

    /*
        # 프로필 검색
        - 응답 : 프로필 유저 정보
        - 조건 : 없음
     */
    @Operation(summary = "프로필 검색", description = "사용자 검색 ")
    @GetMapping("")
    public Response<List<ProfileResponse>> searchProfileInfo(
            @Parameter(description = "조회할 유저 아이디 혹은 닉네임")
            @RequestParam("search") String search) {

        List<ProfileResponse> response = profileService.searchProfileInfo(search);

        return Response.success(response);
    }

//    /*
//        # 프로필 수정
//        - 파라미터 : 닉네임, 프로필 수정, 할 말
//        - 응답 : 프로필 정보
//     */
    @Operation(summary = "프로필 정보 수정", description = "닉네임과 자기소개를 수정합니다.")
    @PatchMapping(value = "/user/{userId}/edit/profile/info")
    @LoginUser
    public Response<ProfileResponse> updateProfileInfo(@AuthenticationPrincipal User user,
                                                       @PathVariable("userId") Long userId,
                                                       @Valid @RequestBody ProfileUpdateRequest request) {
        ProfileResponse response = profileService.editProfileInfo(user, userId, request);

        return Response.success(response);
    }

    /*
        # 프로필 사진 변경
        - 요청 : 파일, 유저 아이디
        - 응답 : 이미지 경로
     */
    @Operation(summary = "프로필 이미지 수정", description = "프로필 이미지를 파일을 S3에 저장 후 Cloudfront 이미지 저장 경로를 응답 합니다.")
    @PutMapping(value = "/user/{userId}/edit/profile/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @LoginUser
    public Response<String> editProfileImage(
                                        @AuthenticationPrincipal User user,
                                        @Parameter(
                                                description = "업로드할 이미지 파일",
                                                content = @Content(mediaType = "multipart/form-data",
                                                        schema = @Schema(type = "string", format = "binary"))
                                        )
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
    @Operation(summary = "댓글 작성", description = "프로필에 댓글을 작성합니다.")
    @PostMapping("/{userId}/comment")
    @LoginUser
    public Response<CommentResponse> addComment(@AuthenticationPrincipal User writer,
                                                @PathVariable("userId") Long userId,
                                                @Valid @RequestBody CommentPostRequest request) {
        CommentResponse response = profileService.postComment(writer, userId, request);
        return Response.success(response);
    }

    @Operation(summary = "댓글 삭제", description = "특정 댓글을 삭제합니다.")
    @DeleteMapping("/{userId}/comment/{commentId}")
    @LoginUser
    public Response<Void> deleteComment(@AuthenticationPrincipal User user,
                                        @PathVariable("userId") Long userId,
                                        @PathVariable("commentId") Long commentId) {

        profileService.deleteComment(user, userId, commentId);

        return Response.success();
    }

    @Operation(summary = "프로필 내 댓글 목록 조회", description = "해당 프로필의 댓글을 페이징 처리하여 조회합니다.")
    @GetMapping("/{userId}/comment")
    public Response<Page<CommentResponse>> getComments(@PathVariable("userId") Long userId,
                                                       @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                       @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                       @ParameterObject Pageable pageable) {

        Page<CommentResponse> responses = profileService.getComments(userId, page, size, pageable);

        return Response.success(responses);
    }

    // 보유 게임 조회
    @Operation(summary = "보유 게임 조회", description = "유저가 보유 중인 게임 리스트를 조회합니다.")
    @GetMapping("/user/{userId}/game")
    public Response<List<MyGameResponse>> getMyGames(@PathVariable("userId") Long userId) {

        List<MyGameResponse> result = profileService.getMyGames(userId);

        return Response.success(result);
    }


}
