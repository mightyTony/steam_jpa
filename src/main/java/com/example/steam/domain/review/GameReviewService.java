package com.example.steam.domain.review;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.game.query.GameRepository;
import com.example.steam.domain.review.dto.GameReviewCreateRequest;
import com.example.steam.domain.review.dto.GameReviewCreateResponse;
import com.example.steam.domain.review.entity.GameReview;
import com.example.steam.domain.review.query.GameReviewRepository;
import com.example.steam.domain.user.User;
import com.example.steam.domain.user.UserRepository;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .reviewType(request.getReviewType()) // TODO 리뷰 타입을 음 어떻게 해야 할 지
                .content(request.getContent())
                .deleted(false)
                .build();

        GameReview savedReview = gameReviewRepository.save(review);
        // 4. toDto
        return gameReviewRepository.findReviewById(savedReview.getId());
    }

    // TODO 성능 개선 필요
    private boolean checkAlreadyWriteReview(User user, Game game) {
        return gameReviewRepository.findByUserAndGame(user,game);
    }
}
