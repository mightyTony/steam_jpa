package com.example.steam.domain.review.query;

import com.example.steam.domain.review.entity.GameReview;

public interface GameReviewLikeRepositoryCustom {
    Long countByGameReview(GameReview gameReview);
}
