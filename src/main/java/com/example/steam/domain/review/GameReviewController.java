package com.example.steam.domain.review;

import com.example.steam.domain.review.dto.GameReviewCreateRequest;
import com.example.steam.domain.review.dto.GameReviewCreateResponse;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class GameReviewController {

    private final GameReviewService reviewService;

    /*
        # 리뷰 작성
        * 한 유저는 한 게임에 오직 한 리뷰만 작성(이미 작성시 수정만 가능)
        * 리뷰 추천/비추천 옵션
        * 리뷰 내용 낫 널
    */
    @PostMapping("/{game}")
    @LoginUser
    public Response<GameReviewCreateResponse> postReview (
            @PathVariable("game") Long gameId,
            @AuthenticationPrincipal User user,
            @Valid @RequestBody GameReviewCreateRequest request) {

        GameReviewCreateResponse response = reviewService.postReview(gameId, user, request);
        return Response.success(response);
    }



    /*
        # 리뷰 수정
        - 기존 리뷰 추천/비추천 변경 가능
        - 내용 수정 가능
     */
//    @PatchMapping("/{reviewId}")


    /*
        # 리뷰 삭제
        - 작성자 본인만 리뷰 삭제 가능
     */
//    @DeleteMapping("/{reviewId}")

    /*
        # 리뷰 목록 조회
        - 특정 게임의 리뷰 목록 조회
        - 추천/비추천 개수 포함
        - 리뷰 작성자 정보 포함
     */
//    @GetMapping("/game/{gameId}")

    /*
        리뷰에 좋아요 누르기
        - 리뷰에는 좋아요는 한번뿐
        - 이미 눌렀다면 또 다시 부를 시 취소
     */
//    @PostMapping("/game/{gameid}/{reviewId}/like")


}
