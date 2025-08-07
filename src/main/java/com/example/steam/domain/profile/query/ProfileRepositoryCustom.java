package com.example.steam.domain.profile.query;

import com.example.steam.domain.profile.dto.ProfileResponse;
import com.example.steam.domain.user.User;

import java.util.List;

public interface ProfileRepositoryCustom {
    ProfileResponse findByUserId(User user, Long userId);

    List<ProfileResponse> findBySearch(String search);
}
