package com.example.steam.domain.profile.query;

import com.example.steam.domain.profile.QProfile;
import com.example.steam.domain.profile.dto.ProfileResponse;
import com.example.steam.domain.profile.dto.QProfileResponse;
import com.example.steam.domain.profile.friendship.Friendship;
import com.example.steam.domain.profile.friendship.QFriendship;
import com.example.steam.domain.user.QUser;
import com.example.steam.domain.user.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryCustomImpl implements ProfileRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QProfile profile = QProfile.profile;
    QUser user = QUser.user;
    @Override
    public ProfileResponse findByUserId(User me, Long userId) {
        QFriendship friendship = QFriendship.friendship;
        return queryFactory
                .select(new QProfileResponse(
                        user.id,
                        profile.content,
                        user.nickname,
                        user.profileImageUrl,
                        friendship.status
                ))
                .from(profile)
                .leftJoin(friendship)
                .on(
                        (friendship.sender.id.eq(me.getId()).and(friendship.receiver.id.eq(userId)))
                        .or
                        (friendship.receiver.id.eq(me.getId()).and(friendship.sender.id.eq(userId)))
                )
                .where(profile.user.id.eq(userId))
                .fetchOne();
    }

    @Override
    public List<ProfileResponse> findBySearch(String search) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause
                .and(ProfilePredicate.idContain(search))
                .and(ProfilePredicate.nameContain(search));

        return queryFactory
                .select(new QProfileResponse(
                        user.id,
                        profile.content,
                        user.nickname,
                        user.profileImageUrl
                ))
                .from(profile)
                .where(whereClause)
                .fetch();
    }

//    @Override
//    public List<ProfileResponse> findBySearch(String search) {
//        BooleanBuilder whereClause = new BooleanBuilder();
//        whereClause
//                .and(ProfilePredicate.idContain(search))
//                .and(ProfilePredicate.nameContain(search));
//
//        return queryFactory
//                .select(new QProfileResponse(
//                        user.id,
//                        profile.content,
//                        user.nickname,
//                        user.profileImageUrl,
//
//                )
//                .from(profile)
//                .where(whereClause)
//                .fetch();
//    }
}
