package com.example.steam.domain.review.query;

import com.example.steam.domain.review.entity.GameReview;
import com.example.steam.domain.review.entity.GameReviewLike;
import com.example.steam.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameReviewLikeRepository extends JpaRepository<GameReviewLike, Long> , GameReviewLikeRepositoryCustom{
    Optional<GameReviewLike> findByUserAndGameReview(User user, GameReview review);
}
