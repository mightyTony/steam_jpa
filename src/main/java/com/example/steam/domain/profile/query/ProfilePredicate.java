package com.example.steam.domain.profile.query;

import com.example.steam.domain.profile.QProfile;
import com.example.steam.domain.user.QUser;
import com.querydsl.core.BooleanBuilder;

import static com.example.steam.domain.profile.QProfile.profile;
import static com.example.steam.domain.user.QUser.user;

public class ProfilePredicate {

    public static BooleanBuilder nameContain(String name) {
        BooleanBuilder builder = new BooleanBuilder();
        if(name != null && !name.isEmpty()){
            builder.and(user.nickname.containsIgnoreCase(name));
        }
        return builder;
    }

    public static BooleanBuilder idContain(String id) {
        BooleanBuilder builder = new BooleanBuilder();
        if(id != null && !id.isEmpty()) {
            builder.and(user.username.containsIgnoreCase(id));
        }
        return builder;
    }
}
