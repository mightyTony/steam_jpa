package com.example.steam.domain.review;

import com.example.steam.domain.review.dto.*;
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

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GameReviewController {

    private final GameReviewService reviewService;

    /*
        # 리뷰 작성
        * 한 유저는 한 게임에 오직 한 리뷰만 작성(이미 작성시 수정만 가능)
        * 리뷰 추천/비추천 옵션
        * 리뷰 내용 낫 널
    */
    @PostMapping("/game/{gameId}/review")
    @LoginUser
    public Response<GameReviewCreateResponse> postReview (
            @PathVariable("gameId") Long gameId,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody GameReviewCreateRequest request) {

        GameReviewCreateResponse response = reviewService.postReview(gameId, user, request);
        log.info("[게임 리뷰 작성] GameId = {}, GameReview = {}", response.getGameId(), response.getReviewId());
        return Response.success(response);
    }

    /*
        # 리뷰 수정
        - 기존 리뷰 추천/비추천 변경 가능
        - 내용 수정 가능
     */
    @LoginUser
    @PatchMapping("/game/{gameId}/review/{reviewId}")
    public Response<GameReviewUpdateResponse> updateGameReview(
            @PathVariable("gameId") Long gameId,
            @PathVariable("reviewId") Long reviewId,
            @Valid @RequestBody GameReviewUpdateRequest request,
            @AuthenticationPrincipal User user) {

        GameReviewUpdateResponse response = reviewService.updateGameReview(gameId, reviewId, request, user);
        log.info("[게임 리뷰 수정] GameId = {}, ReviewId = {}", response.getGameId(), response.getReviewId());
        return Response.success(response);
    }


    /*
        # 리뷰 삭제
        - 작성자 본인만 리뷰 삭제 가능
     */
    @LoginUser
    @DeleteMapping("/review/{reviewId}")
    public Response<String> deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);

        return Response.success("삭제 되었습니다");
    }

    /*
        # 리뷰 목록 조회 (페이지네이션)
        - 특정 게임의 리뷰 목록 조회
        - 추천/비추천 개수 포함
        - 리뷰 작성자 정보 포함
        - 좋아요 순 / 최신 순 /
     */
    @GetMapping("/game/{gameId}/review")
    public Response<Page<GameReviewReadResponse>> getGameReview(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            Pageable pageable,
            @PathVariable("gameId") Long gameId,
            @RequestParam(name = "sort", required = false, defaultValue = "like") String sortBy) {

        Page<GameReviewReadResponse> response = reviewService.getGameReviewPaging(page, size, pageable, gameId, sortBy);
//        log.info("[리뷰 조회] {} ", response.getContent());
        return Response.success(response);
    }

    /*
        리뷰에 좋아요 누르기
        - 리뷰에는 좋아요는 한번뿐
        - 이미 눌렀다면 또 다시 부를 시 취소
     */
    @LoginUser
    @PostMapping("/game/{gameId}/{reviewId}/like")
    public Response<GameReviewLikeResponse> toggleReviewLike(@AuthenticationPrincipal User user,
                                                             @PathVariable("gameId") Long gameId,
                                                             @PathVariable("reviewId") Long reviewId,
                                                             @RequestParam("toggle") boolean toggleLike) {
        GameReviewLikeResponse response = reviewService.toggleReviewLike(user, gameId, reviewId, toggleLike);

        return Response.success(response);
    }


}
