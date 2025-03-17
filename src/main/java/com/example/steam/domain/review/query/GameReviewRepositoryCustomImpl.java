package com.example.steam.domain.review.query;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.game.QGame;
import com.example.steam.domain.review.dto.GameReviewCreateResponse;
import com.example.steam.domain.review.dto.GameReviewReadResponse;
import com.example.steam.domain.review.dto.QGameReviewCreateResponse;
import com.example.steam.domain.review.dto.QGameReviewReadResponse;
import com.example.steam.domain.review.entity.QGameReview;
import com.example.steam.domain.review.entity.QGameReviewLike;
import com.example.steam.domain.user.QUser;
import com.example.steam.domain.user.User;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.objenesis.SpringObjenesis;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GameReviewRepositoryCustomImpl implements GameReviewRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QGame game = QGame.game;
    QUser user = QUser.user;
    QGameReview review = QGameReview.gameReview;
    QGameReviewLike reviewLike = QGameReviewLike.gameReviewLike;

    // TODO jojoldu
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
                        review.recommend,
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

    @Override
    public Page<GameReviewReadResponse> findGameReviewWithPaging(int page, int size, Pageable pageable, Long gameId, String sortBy) {
        List<GameReviewReadResponse> results = queryFactory
                .select(new QGameReviewReadResponse(
                        review.id,
                        user.username,
                        user.nickname,
                        review.recommend,
                        reviewLike.id.count().intValue(),
                        review.content,
                        review.createdAt.stringValue(),
                        review.lastModifiedAt.stringValue()
                ))
                .from(review)
                .leftJoin(review.user, user)
                .leftJoin(review.likes, reviewLike)
                .where(
                        review.game.id.eq(gameId),
                        review.deleted.eq(false)
                )
                .groupBy(review.id)
                .orderBy(getSortOrder(sortBy))
                .offset(page)
                .limit(size)
                .fetch();

        // 전체 개수
        long total = Optional.ofNullable(queryFactory
                .select(review.count())
                .from(review)
                .where(review.game.id.eq(gameId))
                .fetchOne())
                .orElse(0L);

        return new PageImpl<>(results, pageable, total);
    }

    private OrderSpecifier<?> getSortOrder(String sortBy) {
        // 좋아요 순
        if("like".equalsIgnoreCase(sortBy)){
            return reviewLike.id.count().desc();
        }
        // 최신 순
        return review.createdAt.desc();
    }

}
