package com.example.steam.domain.wish.query;

import com.example.steam.domain.user.User;
import com.example.steam.domain.wish.Wish;
import com.example.steam.domain.wish.dto.WishDto;

import java.util.List;
import java.util.Optional;

public interface WishRepositoryCustom {
    boolean existsWishByGameIdAndUser(Long gameId, User user);

    List<WishDto> findAllByUser_Id(Long userId);
}
