package com.example.steam.domain.review.query;

import com.example.steam.domain.review.entity.GameReview;
import com.example.steam.domain.review.entity.QGameReviewLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GameReviewLikeRepositoryCustomImpl implements GameReviewLikeRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    QGameReviewLike like = QGameReviewLike.gameReviewLike;

    @Override
    public Long countByGameReview(GameReview gameReview) {
        return queryFactory
                .select(like.count())
                .from(like)
                .where(like.gameReview.eq(gameReview))
                .fetchOne();
    }
}
