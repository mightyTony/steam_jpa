package com.example.steam.domain.game.query;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.game.dto.GameDetailResponse;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.steam.domain.game.QGame.game;
import static com.example.steam.domain.game.genre.QGameGenre.gameGenre;

@Repository
@RequiredArgsConstructor
public class GameRepositoryCustomImpl implements GameRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GameDetailResponse> findGamesByCategory(String category, String name, Integer minPrice, Integer maxPrice, Pageable pageable) {
        BooleanBuilder whereClause = new BooleanBuilder();

        whereClause
                .and(GamePredicate.nameContain(name))
//                .and(GamePredicate.genreEquals(genre))
                .and(GamePredicate.priceBetween(minPrice, maxPrice));

//        QGame game = QGame.game;
//        QGameGenre genre = QGameGenre.gameGenre;

        List<Game> games = queryFactory
                .selectFrom(game)
                .distinct()
                .leftJoin(game.genres, gameGenre)
                .where(whereClause)
                .orderBy(getOrderSpecifier(category))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<GameDetailResponse> results = games.stream()
                .map(GameDetailResponse::new)
                .collect(Collectors.toList());


        long total = queryFactory
                .select(game.count())
                .from(game)
                .where(whereClause)
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(String category) {
        /*
        if(category.equals("top")){
            return game.sales.desc();
        }
        else if(category.equals("new"))
            return game.createdAt.desc();
        else if(category.equals("hotsale"))
            return game.discount.desc();
        else return game.id.desc();
         */
        return switch (category) {
            case "top" -> game.sales.desc();
            case "new" -> game.createdAt.desc();
            case "hotsale" -> game.discount.desc();
            default -> game.id.desc();
        };
    }
}
