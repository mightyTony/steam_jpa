package com.example.steam.domain.review;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.game.query.GameRepository;
import com.example.steam.domain.review.dto.*;
import com.example.steam.domain.review.entity.GameReview;
import com.example.steam.domain.review.query.GameReviewRepository;
import com.example.steam.domain.user.User;
import com.example.steam.domain.user.UserRepository;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.CloseableThreadContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameReviewService {

    private final GameReviewRepository gameReviewRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    @Transactional
    public GameReviewCreateResponse postReview(Long gameId, User user, GameReviewCreateRequest request) {
        // 1. 게임 체크
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME));
        // 2. 리뷰 글 이미 썼는 지 체크
        if(checkAlreadyWriteReview(user,game)) {
            throw new SteamException(ErrorCode.REVIEW_ALREADY_WRITE);
        }
        // 3. 리뷰 생성
        GameReview review = GameReview.builder()
                .user(user)
                .game(game)
                .recommend(request.isRecommend())
                .content(request.getContent())
                .deleted(false)
                .build();

        GameReview savedReview = gameReviewRepository.save(review);
        // 4. toDto
        return gameReviewRepository.findReviewById(savedReview.getId());
    }

    // TODO 성능 개선 필요 https://jojoldu.tistory.com/516
    private boolean checkAlreadyWriteReview(User user, Game game) {
        return gameReviewRepository.findByUserAndGame(user,game);
    }

    @Transactional
    public GameReviewUpdateResponse updateGameReview(
            Long gameId, Long reviewId, GameReviewUpdateRequest request, User user) {

        // 1. 게임 검증
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME));
        // 2. 리뷰가 있는 지 검증
        GameReview gameReview = gameReviewRepository.findGameReviewById(reviewId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME_REVIEW));
        // 3. 수정
        gameReview.update(request.getRecommend(),request.getContent());

        GameReview savedReview = gameReviewRepository.save(gameReview);

        return GameReviewUpdateResponse.fromEntity(savedReview);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        // 리뷰 검증
        GameReview review = gameReviewRepository.findById(reviewId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME_REVIEW));

        // 리뷰 삭제(soft)
        review.delete();

        log.info("[리뷰 삭제] 리뷰가 삭제 되었습니다. 리뷰 게임 : {} / 리뷰 아이디 : {}", review.getGame().getName(), review.getId());

        gameReviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public Page<GameReviewReadResponse> getGameReviewPaging(int page, int size, Pageable pageable, Long gameId, String sortBy) {
        Page<GameReviewReadResponse> result = gameReviewRepository.findGameReviewWithPaging(page, size, pageable, gameId, sortBy);

        if(result.isEmpty()) {
            throw new SteamException(ErrorCode.NOT_FOUND_GAME_REVIEW);
        }
        return result;
    }
}
