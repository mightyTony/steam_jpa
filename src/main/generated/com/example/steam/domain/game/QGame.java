package com.example.steam.domain.game;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGame is a Querydsl query type for Game
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGame extends EntityPathBase<Game> {

    private static final long serialVersionUID = 307380179L;

    public static final QGame game = new QGame("game");

    public final com.example.steam.util.QBaseEntity _super = new com.example.steam.util.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath developer = createString("developer");

    public final NumberPath<Integer> discount = createNumber("discount", Integer.class);

    public final ListPath<com.example.steam.domain.game.genre.GameGenre, com.example.steam.domain.game.genre.QGameGenre> genres = this.<com.example.steam.domain.game.genre.GameGenre, com.example.steam.domain.game.genre.QGameGenre>createList("genres", com.example.steam.domain.game.genre.GameGenre.class, com.example.steam.domain.game.genre.QGameGenre.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final StringPath name = createString("name");

    public final BooleanPath onSale = createBoolean("onSale");

    public final StringPath pictureUrl = createString("pictureUrl");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath publisher = createString("publisher");

    public final StringPath releaseDate = createString("releaseDate");

    public final NumberPath<Integer> sales = createNumber("sales", Integer.class);

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public QGame(String variable) {
        super(Game.class, forVariable(variable));
    }

    public QGame(Path<? extends Game> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGame(PathMetadata metadata) {
        super(Game.class, metadata);
    }

}

