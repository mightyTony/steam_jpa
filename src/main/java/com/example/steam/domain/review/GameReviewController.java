package com.example.steam.domain.review;

import com.example.steam.domain.review.dto.*;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "게임 리뷰", description = "게임 리뷰 관련 API")
public class GameReviewController {

    private final GameReviewService reviewService;

    /*
        # 리뷰 작성
        * 한 유저는 한 게임에 오직 한 리뷰만 작성(이미 작성시 수정만 가능)
        * 리뷰 추천/비추천 옵션
        * 리뷰 내용 낫 널
    */
    @Operation(summary = "리뷰 작성", description = "유저가 특정 게임에 리뷰를 작성합니다. (게임당 리뷰 1개 제한)")
    @PostMapping("/game/{gameId}/review")
    @LoginUser
    public Response<GameReviewCreateResponse> postReview (
            @Parameter(description = "게임 ID", example = "1")
            @PathVariable("gameId") Long gameId,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody GameReviewCreateRequest request) {

        GameReviewCreateResponse response = reviewService.postReview(gameId, user, request);
        log.info("[LOG] [게임 리뷰 작성] GameId = {}, GameReview = {}", response.getGameId(), response.getReviewId());
        return Response.success(response);
    }

    /*
        # 리뷰 수정
        - 기존 리뷰 추천/비추천 변경 가능
        - 내용 수정 가능
     */
    @Operation(summary = "리뷰 수정", description = "작성한 리뷰를 수정합니다. 내용 및 추천 변경 가능")
    @LoginUser
    @PatchMapping("/game/{gameId}/review/{reviewId}")
    public Response<GameReviewUpdateResponse> updateGameReview(
            @PathVariable("gameId") Long gameId,
            @PathVariable("reviewId") Long reviewId,
            @Valid @RequestBody GameReviewUpdateRequest request,
            @AuthenticationPrincipal User user) {

        GameReviewUpdateResponse response = reviewService.updateGameReview(gameId, reviewId, request, user);
        log.info("[LOG] [게임 리뷰 수정] GameId = {}, ReviewId = {}", response.getGameId(), response.getReviewId());
        return Response.success(response);
    }


    /*
        # 리뷰 삭제
        - 작성자 본인만 리뷰 삭제 가능
     */
    @Operation(summary = "리뷰 삭제", description = "유저가 작성한 리뷰를 삭제합니다.")
    @LoginUser
    @DeleteMapping("/review/{reviewId}")
    public Response<String> deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
        log.info("[리뷰 / 삭제] 리뷰 id - {}", reviewId);
        return Response.success("삭제 되었습니다");
    }

    /*
        # 리뷰 목록 조회 (페이지네이션)
        - 특정 게임의 리뷰 목록 조회
        - 추천/비추천 개수 포함
        - 리뷰 작성자 정보 포함
        - 좋아요 순 / 최신 순 /
     */
    @Operation(summary = "리뷰 목록 조회", description = "특정 게임의 리뷰를 페이지네이션하여 조회합니다. 최신순/좋아요순 정렬 가능")
    @GetMapping("/game/{gameId}/review")
    public Response<Page<GameReviewReadResponse>> getGameReview(
            @Parameter(description = "페이지 번호", example = "0")  @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            Pageable pageable,
            @Parameter(description = "게임 ID", example = "1")    @PathVariable("gameId") Long gameId,
            @Parameter(description = "정렬 기준 (like: 좋아요순 / 없으면 최신순)", example = "like") @RequestParam(name = "sort", required = false, defaultValue = "like") String sortBy) {

        Page<GameReviewReadResponse> response = reviewService.getGameReviewPaging(page, size, pageable, gameId, sortBy);
//        log.info("[리뷰 조회] {} ", response.getContent());
        return Response.success(response);
    }

    /*
        리뷰에 좋아요 누르기
        - 리뷰에는 좋아요는 한번뿐
        - 이미 눌렀다면 또 다시 부를 시 취소
     */
    @Operation(summary = "리뷰 좋아요/취소", description = "리뷰에 좋아요를 누르거나 취소합니다. 토글 형식")
    @LoginUser
    @PostMapping("/game/{gameId}/{reviewId}/like")
    public Response<GameReviewLikeResponse> toggleReviewLike(@AuthenticationPrincipal User user,
                                                             @PathVariable("gameId") Long gameId,
                                                             @PathVariable("reviewId") Long reviewId,
                                                             @Parameter(name = "toggle", description = "true = 좋아요/ false = 취소")
                                                                 @RequestParam("toggle") boolean toggleLike) {
        GameReviewLikeResponse response = reviewService.toggleReviewLike(user, gameId, reviewId, toggleLike);

        return Response.success(response);
    }


}
