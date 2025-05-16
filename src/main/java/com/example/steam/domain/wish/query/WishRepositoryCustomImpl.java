package com.example.steam.domain.wish.query;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.game.QGame;
import com.example.steam.domain.user.User;
import com.example.steam.domain.wish.QWish;
import com.example.steam.domain.wish.dto.QWishDto;
import com.example.steam.domain.wish.dto.WishDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WishRepositoryCustomImpl implements WishRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QWish wish = QWish.wish;
//    private final QUser user = QUser.user;
    private final QGame game = QGame.game;

    @Override
    public boolean existsWishByGameIdAndUser(Long gameId, User user) {
        Integer result = queryFactory.selectOne()
                .from(wish)
                .where(wish.game.id.eq(gameId), wish.user.eq(user))
                .fetchFirst();
        return result != null;
    }

    @Override
    public List<WishDto> findAllByUser_Id(Long userId) {
        return queryFactory.select(new QWishDto(
                wish.id,
                game.id,
                game.name,
                game.developer,
                game.publisher,
                game.content,
                game.price,
                game.discount,
                game.totalPrice,
                game.pictureUrl,
                game.onSale
                ))
                .from(wish)
                .join(wish.game,game)
                .where(wish.user.id.eq(userId))
                .fetch();

    }

    @Override
    public List<Long> findUsersByGame(Game Ggame) {

        return queryFactory.select(wish.user.id)
                .from(wish)
                .join(wish.game, game)
                .where(wish.game.eq(Ggame))
                .fetch();
    }
}
