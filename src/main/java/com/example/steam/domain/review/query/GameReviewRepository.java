package com.example.steam.domain.review.query;

import com.example.steam.domain.review.entity.GameReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameReviewRepository extends JpaRepository<GameReview, Long>,GameReviewRepositoryCustom {
//    boolean findByUserAndGame(User user, Game game);
    Optional<GameReview> findGameReviewById(Long reviewId);

    Optional<GameReview> findAllByGameId(Long gameId);
}
