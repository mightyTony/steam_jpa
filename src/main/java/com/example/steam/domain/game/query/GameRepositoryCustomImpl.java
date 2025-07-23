package com.example.steam.domain.game.query;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.game.QGame;
import com.example.steam.domain.game.dto.GameDetailResponse;
import com.example.steam.domain.game.dto.GameRankingResponse;
import com.example.steam.domain.game.dto.QGameRankingResponse;
import com.example.steam.domain.game.genre.QGameGenre;
import com.example.steam.domain.order.OrderStatus;
import com.example.steam.domain.order.QOrder;
import com.example.steam.domain.order.QOrderItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class GameRepositoryCustomImpl implements GameRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QGame game = QGame.game;
    QGameGenre gameGenre = QGameGenre.gameGenre;

    @Override
    public Page<GameDetailResponse> findGamesByCategory(String category, String name, Integer minPrice,
                                                        Integer maxPrice, int page, int size) {
        BooleanBuilder whereClause = new BooleanBuilder();

        whereClause
                .and(GamePredicate.nameContain(name))
//                .and(GamePredicate.genreEquals(genre))
                .and(GamePredicate.priceBetween(minPrice, maxPrice));

        List<Game> games = queryFactory
                .selectFrom(game)
                .distinct()
                .leftJoin(game.genres, gameGenre)
                .where(whereClause)
                .orderBy(getOrderSpecifier(category))
                .offset(page)
                .limit(size)
                .fetch();

        List<GameDetailResponse> results = games.stream()
                .map(GameDetailResponse::new)
                .collect(Collectors.toList());

        long total = queryFactory
                .select(game.count())
                .from(game)
                .where(whereClause)
                .fetchOne();

        Pageable pageable = PageRequest.of(page, size);
        return new PageImpl<>(results, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(String category) {
        if (category == null || category.isBlank()) {
            return game.sales.desc();
        }
        return switch (category) {
            case "top" -> game.sales.desc();
            case "new" -> game.createdAt.desc();
            case "hotsale" -> game.discount.desc();
            default -> game.id.desc();
        };
    }

    @Override
    public List<GameRankingResponse> findTopGamesBySales(LocalDateTime since) {
        QGame game = QGame.game;
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;

        return queryFactory
                .select(new QGameRankingResponse(
                        game.id,
                        game.name,
                        game.developer,
                        game.publisher,
                        game.price,
                        game.totalPrice,
                        game.pictureUrl,
                        game.sales,
                        game.discount,
                        game.releaseDate,
                        game.likeCount,
                        game.dislikeCount
                ))
                .from(game)
                .leftJoin(orderItem).on(orderItem.game.eq(game))
                .leftJoin(order).on(orderItem.order.eq(order))
                .where(order.createdAt.after(since)
                        .and(order.status.eq(OrderStatus.COMPLETED)))
                .groupBy(game.id)
                .orderBy(orderItem.count().desc())
                .limit(50)
                .fetch();
    }

    @Override
    public List<GameRankingResponse> findTopGames() {
        QGame game = QGame.game;
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;

        return queryFactory
                .select(new QGameRankingResponse(
                        game.id,
                        game.name,
                        game.developer,
                        game.publisher,
                        game.price,
                        game.totalPrice,
                        game.pictureUrl,
                        game.sales,
                        game.discount,
                        game.releaseDate,
                        game.likeCount,
                        game.dislikeCount
                ))
                .from(game)
//                .leftJoin(orderItem).on(orderItem.game.eq(game))
//                .leftJoin(order).on(orderItem.order.eq(order))
//                .where(order.status.eq(OrderStatus.COMPLETED))
                .where(game.onSale.isTrue())
                .groupBy(game.id)
                .orderBy(game.sales.desc())
                .limit(50)
                .fetch();
    }


}
