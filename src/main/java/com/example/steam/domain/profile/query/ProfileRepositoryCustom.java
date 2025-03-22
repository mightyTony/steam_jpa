package com.example.steam.domain.profile.query;

import com.example.steam.domain.profile.dto.ProfileResponse;

public interface ProfileRepositoryCustom {
    ProfileResponse findByUserId(Long userId);
}
