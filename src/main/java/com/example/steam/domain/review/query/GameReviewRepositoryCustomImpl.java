package com.example.steam.domain.review.query;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.game.QGame;
import com.example.steam.domain.review.dto.GameReviewCreateResponse;
import com.example.steam.domain.review.dto.QGameReviewCreateResponse;
import com.example.steam.domain.review.entity.QGameReview;
import com.example.steam.domain.user.QUser;
import com.example.steam.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GameReviewRepositoryCustomImpl implements GameReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QGame game = QGame.game;
    QUser user = QUser.user;
    QGameReview review = QGameReview.gameReview;

    @Override
    public boolean findByUserAndGame(User user, Game game) {
        Integer fetchOne = queryFactory
                .selectOne()
                .from(review)
                .where(
                        review.user.eq(user),
                        review.game.eq(game)
                )
                .fetchFirst();

        return fetchOne != null; // 1개가 있는 지 없는 지 판단
    }

    @Override
    public GameReviewCreateResponse findReviewById(Long reviewId) {
        return queryFactory
                .select(new QGameReviewCreateResponse(
                        review.id,
                        game.id,
                        review.content,
                        review.reviewType,
                        review.deleted,
                        review.createdAt.stringValue(),
                        review.lastModifiedAt.stringValue(),
                        user.username,
                        user.nickname,
                        user.profileImageUrl
                ))
                .from(review)
                .join(review.user, user)
                .join(review.game, game)
                .where(review.id.eq(reviewId))
                .fetchOne();
    }
}
