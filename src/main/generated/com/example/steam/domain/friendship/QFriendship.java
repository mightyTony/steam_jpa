package com.example.steam.domain.friendship;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFriendship is a Querydsl query type for Friendship
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFriendship extends EntityPathBase<Friendship> {

    private static final long serialVersionUID = -1585128877L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFriendship friendship = new QFriendship("friendship");

    public final com.example.steam.util.QBaseEntity _super = new com.example.steam.util.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.example.steam.domain.user.QUser friend;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final EnumPath<FriendStatus> status = createEnum("status", FriendStatus.class);

    public final com.example.steam.domain.user.QUser user;

    public QFriendship(String variable) {
        this(Friendship.class, forVariable(variable), INITS);
    }

    public QFriendship(Path<? extends Friendship> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFriendship(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFriendship(PathMetadata metadata, PathInits inits) {
        this(Friendship.class, metadata, inits);
    }

    public QFriendship(Class<? extends Friendship> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.friend = inits.isInitialized("friend") ? new com.example.steam.domain.user.QUser(forProperty("friend")) : null;
        this.user = inits.isInitialized("user") ? new com.example.steam.domain.user.QUser(forProperty("user")) : null;
    }

}

