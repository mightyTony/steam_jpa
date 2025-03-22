package com.example.steam.domain.profile.friendship;

import com.example.steam.domain.profile.friendship.dto.*;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/friendship")
public class FriendshipController {

    private final FriendshipService friendService;

    /*
        # 친구 신청
        - (대기 상태 생성), 보낸 이, 받는 이
        - 보낸 이 = 로그인 한 유저
        - 받는 이 있는 지 검증
     */
    @LoginUser
    @PostMapping("/request")
    public Response<Void> requestFriendship(@AuthenticationPrincipal User user,
                                            @Valid @RequestBody FriendshipRequest request) {
        Friendship friendship = friendService.requestFriendship(user, request);
        log.info("[친구 신청] response : {}", friendship);
        return Response.success();
    }

    /*
        # 친구 요청 목록 조회
        - 응답 값 : 유저 id, 유저네임(실질적 아이디) 닉네임, 프로필 사진
        - 조건 : 대기 상태, 받는 이 = 나
     */
    @LoginUser
    @GetMapping("/request/received")
    public Response<List<FriendshipResponse>> getFriendshipRequest(@AuthenticationPrincipal User user) {
        List<FriendshipResponse> response = friendService.getFriendshipRequest(user);
        log.info("[친구 요청 목록 조회] - response : {}", response);
        return Response.success(response);
    }

    /*
        # 친구 요청 승락/거절
        - 승락 시 : 대기 상태 -> 수락 상태 변경
        - 거절 시 : 친구 관계 삭제
        - 이외의 FriendStatus 값 검증 예외 처리
//     */
    @LoginUser
    @PatchMapping("/request/{requestId}")
    public Response<FriendshipResponse> updateFriendshipRequestStatus(@PathVariable("requestId") Long requestId,
                                                                      @RequestBody FriendshipRequestUpdateDto requestDto) {
        FriendshipResponse response = friendService.updateFriendshipRequestStatus(requestId, requestDto);

        return Response.success(response);
    }

    /*
        # 내가 보낸 친구 요청 (대기중 인것) 조회
        - 조건 : 보낸 이 = 나, 상태 = 대기 중
     */
    @GetMapping("/request/search")
    @LoginUser
    public Response<List<FriendshipSearchResponse>> getMyFriendshipRequest(@AuthenticationPrincipal User user) {
        log.info("[내가 보낸 친구 요청 (대기중 인것) 조회]");
        List<FriendshipSearchResponse> result = friendService.getMyFriendshipRequest(user);
        log.info("[내가 보낸 친구 요청 (대기중 인것) 조회] - {} ", result);
        return Response.success(result);
    }

    /*
        # 내가 보낸 친구 요청 (대기중 인것) 삭제
        - 친구 관계 삭제
     */
    @LoginUser
    @DeleteMapping("/request/{requestId}")
    public Response<Void> deleteMyFriendshipRequest(@AuthenticationPrincipal User user,
                                                    @PathVariable("requestId") Long requestId) {
        friendService.deleteMyFriendshipRequest(user, requestId);

        return Response.success();
    }

    /*
        # 내 친구 조회
        - 조회 조건 : 보낸이 id = 나 or 받은이 id = 나, 친구 상태(FriendStatus) = ACCEPTED
        - 응답 : 유저 id, 아이디, 닉네임, 프로필 사진
     */
    @LoginUser
    @GetMapping("/friends")
    public Response<List<FriendshipReadResponse>> getMyFriends(@AuthenticationPrincipal User user) {
        log.info("[내 친구 조회]");
        List<FriendshipReadResponse> responses = friendService.getMyFriends(user);
        log.info("[내 친구 조회] - {}", responses);
        return Response.success(responses);
    }

    /*
        # 내 친구 조회 2 (프로필 화면에서 친구 6명 보여주기용)
        - 조회 조건 : 나와 친구이고 상태 ACCEPTED
        - 응답 : 친구 리스트(6명), 총 친구 수(count)
     */
    @LoginUser
    @GetMapping("/friends-short")
    public Response<FriendshipShortViewResponse> getMyFriendsList(@AuthenticationPrincipal User user) {
        log.info("[내 친구 조회 2 (프로필 화면에서 친구 6명 보여주기용)]");
        FriendshipShortViewResponse response = friendService.getMyFriendsList(user);
        log.info("[내 친구 조회 2 (프로필 화면에서 친구 6명 보여주기용)] - {}", response);
        return Response.success(response);
    }
}