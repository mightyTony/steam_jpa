package com.example.steam.domain.game;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMyGame is a Querydsl query type for MyGame
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMyGame extends EntityPathBase<MyGame> {

    private static final long serialVersionUID = -767584417L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMyGame myGame = new QMyGame("myGame");

    public final com.example.steam.util.QBaseEntity _super = new com.example.steam.util.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final QGame game;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final com.example.steam.domain.user.QUser user;

    public QMyGame(String variable) {
        this(MyGame.class, forVariable(variable), INITS);
    }

    public QMyGame(Path<? extends MyGame> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMyGame(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMyGame(PathMetadata metadata, PathInits inits) {
        this(MyGame.class, metadata, inits);
    }

    public QMyGame(Class<? extends MyGame> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.game = inits.isInitialized("game") ? new QGame(forProperty("game")) : null;
        this.user = inits.isInitialized("user") ? new com.example.steam.domain.user.QUser(forProperty("user")) : null;
    }

}

