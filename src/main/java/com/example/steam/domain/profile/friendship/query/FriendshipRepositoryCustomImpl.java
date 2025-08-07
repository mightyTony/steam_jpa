package com.example.steam.domain.profile.friendship.query;

import com.example.steam.domain.profile.friendship.FriendStatus;
import com.example.steam.domain.profile.friendship.QFriendship;
import com.example.steam.domain.profile.friendship.dto.*;
import com.example.steam.domain.user.QUser;
import com.example.steam.domain.user.User;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FriendshipRepositoryCustomImpl implements FriendshipRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QFriendship friendship = QFriendship.friendship;
    QUser qUser = QUser.user;
    @Override
    public boolean isFriend(Long myId, Long friendId) {


        Integer count = queryFactory
                .selectOne()
                .from(friendship)
                .where(
                        (friendship.sender.id.eq(myId).and(friendship.receiver.id.eq(friendId)))
                                .or(friendship.sender.id.eq(friendId).and(friendship.receiver.id.eq(myId))),
                        friendship.status.eq(FriendStatus.ACCEPTED).or(friendship.status.eq(FriendStatus.PENDING)),
                        // 자기 자신한테 보내는 거 막기
                        friendship.sender.id.ne(friendship.receiver.id)
                )
                .fetchFirst();

        return count != null;
    }

    @Override
    public Optional<List<FriendshipReadResponse>> findFriendships(Long userId) {
        QUser sender = new QUser("sender");
        QUser receiver = new QUser("receiver");

        // 내가 친구 요청 보낸 경우
        List<FriendshipReadResponse> list1 = queryFactory
                .select(new QFriendshipReadResponse(
                        receiver.id,
                        receiver.username,
                        receiver.nickname,
                        receiver.profileImageUrl,
                        friendship.id
                ))
                .from(friendship)
                .leftJoin(friendship.receiver, receiver)
                .where(friendship.sender.id.eq(userId)
                        .and(friendship.receiver.deleted.eq(false))
                        .and(friendship.status.eq(FriendStatus.ACCEPTED))
                )
                .fetch();

        // 내가 친구 요청 받은 경우
        List<FriendshipReadResponse> list2 = queryFactory
                .select(new QFriendshipReadResponse(
                        sender.id,
                        sender.username,
                        sender.nickname,
                        sender.profileImageUrl,
                        friendship.id
                ))
                .from(friendship)
                .leftJoin(friendship.sender, sender)
                .where(friendship.receiver.id.eq(userId)
                        .and(friendship.sender.deleted.eq(false))
                        .and(friendship.status.eq(FriendStatus.ACCEPTED))
                )
                .fetch();

        // querydsl엔 union 지원 안되서 그냥 합쳐야 하는듯..

        // 합치기
        List<FriendshipReadResponse> result = new ArrayList<>();
        result.addAll(list1);
        result.addAll(list2);

        return Optional.of(result);
    }

    /*
    # 내가 보낸 친구 요청 (대기중 인것) 조회
    - 조건 : 보낸 이 = 나, 상태 = 대기 중
    */
    @Override
    public List<FriendshipSearchResponse> getMyFriendshipRequest(User user) {
        return queryFactory
                .select(new QFriendshipSearchResponse(
                        friendship.id,
                        friendship.sender.id,
                        friendship.receiver.id,
                        friendship.receiver.nickname,
                        friendship.status,
                        friendship.receiver.profileImageUrl
                ))
                .from(friendship)
                .where(
                        friendship.sender.id.eq(user.getId())
                                .and(friendship.status.eq(FriendStatus.PENDING))
                )
                .fetch();
    }

    @Override
    public FriendshipShortViewResponse getMyFriendsListShort(Long userId) {
        QFriendship friendship = QFriendship.friendship;

        Long total = queryFactory
                .select(friendship.count())
                .from(friendship)
                .where(
                        (friendship.sender.id.eq(userId).or(friendship.receiver.id.eq(userId)))
                                .and(friendship.status.eq(FriendStatus.ACCEPTED))
                                .and(friendship.sender.id.ne(friendship.receiver.id))
                )
                .fetchFirst();

        List<FriendshipReadResponse> friends = queryFactory
                .select(new QFriendshipReadResponse(
                        new CaseBuilder()
                                .when(friendship.sender.id.eq(userId))
                                .then(friendship.receiver.id)
                                .otherwise(friendship.sender.id),
                        new CaseBuilder()
                                .when(friendship.sender.id.eq(userId))
                                .then(friendship.receiver.username)
                                .otherwise(friendship.sender.username),
                        new CaseBuilder()
                                .when(friendship.sender.id.eq(userId))
                                .then(friendship.receiver.nickname)
                                .otherwise(friendship.sender.nickname),
                        new CaseBuilder()
                                .when(friendship.sender.id.eq(userId))
                                .then(friendship.receiver.profileImageUrl)
                                .otherwise(friendship.sender.profileImageUrl),
                        friendship.id
                ))
                .from(friendship)
                .where(
                        (friendship.sender.id.eq(userId).or(friendship.receiver.id.eq(userId)))
                                .and(friendship.status.eq(FriendStatus.ACCEPTED))
                                .and(friendship.sender.id.ne(friendship.receiver.id))
                )
                .limit(6)
                .fetch();

        return new FriendshipShortViewResponse(total, friends);
    }
}
