package com.example.steam.domain.review;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGameReview is a Querydsl query type for GameReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGameReview extends EntityPathBase<GameReview> {

    private static final long serialVersionUID = 1998800037L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGameReview gameReview = new QGameReview("gameReview");

    public final com.example.steam.util.QBaseEntity _super = new com.example.steam.util.QBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final com.example.steam.domain.user.QUser user;

    public QGameReview(String variable) {
        this(GameReview.class, forVariable(variable), INITS);
    }

    public QGameReview(Path<? extends GameReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGameReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGameReview(PathMetadata metadata, PathInits inits) {
        this(GameReview.class, metadata, inits);
    }

    public QGameReview(Class<? extends GameReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.steam.domain.user.QUser(forProperty("user")) : null;
    }

}

