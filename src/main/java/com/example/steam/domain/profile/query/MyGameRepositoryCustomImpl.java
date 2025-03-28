package com.example.steam.domain.profile.query;

import com.example.steam.domain.game.QGame;
import com.example.steam.domain.profile.dto.MyGameResponse;
import com.example.steam.domain.profile.dto.QMyGameResponse;
import com.example.steam.domain.profile.mygame.QMyGame;
import com.example.steam.domain.user.QUser;
import com.example.steam.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MyGameRepositoryCustomImpl implements MyGameRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QMyGame myGame = QMyGame.myGame;
    QGame game =QGame.game;
    QUser user = QUser.user;

    @Override
    public List<MyGameResponse> getMyGamesByUser(User user) {

        return queryFactory.
                select(new QMyGameResponse(
                        myGame.id,
                        myGame.game.id,
                        myGame.game.name,
                        myGame.game.developer,
                        myGame.game.publisher,
                        myGame.game.pictureUrl
                ))
                .from(myGame)
                .leftJoin(myGame.game, game)
                .where(myGame.user.eq(user))
                .orderBy(myGame.createdAt.desc())
                .fetch();
    }
}
