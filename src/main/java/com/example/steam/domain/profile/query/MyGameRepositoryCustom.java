package com.example.steam.domain.profile.query;

import com.example.steam.domain.profile.dto.MyGameResponse;
import com.example.steam.domain.user.User;

import java.util.List;

public interface MyGameRepositoryCustom {
    List<MyGameResponse> getMyGamesByUser(User user);
}
