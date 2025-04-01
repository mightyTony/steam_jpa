package com.example.steam.domain.profile.query;

import com.example.steam.domain.profile.QProfile;
import com.example.steam.domain.profile.dto.ProfileResponse;
import com.example.steam.domain.profile.dto.QProfileResponse;
import com.example.steam.domain.user.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProfileRepositoryCustomImpl implements ProfileRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QProfile profile = QProfile.profile;
    QUser user = QUser.user;
    @Override
    public ProfileResponse findByUserId(Long userId) {
        return queryFactory
                .select(new QProfileResponse(
                        user.id,
                        profile.content,
                        user.nickname,
                        user.profileImageUrl
                ))
                .from(profile)
                .where(profile.user.id.eq(userId))
                .fetchOne();
    }
}
