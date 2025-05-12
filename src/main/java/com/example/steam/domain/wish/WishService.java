package com.example.steam.domain.wish;

import com.example.steam.domain.user.User;
import com.example.steam.domain.wish.dto.WishDto;

import java.util.List;

public interface WishService {
    void addToWish(Long gameId, User user);

    void removeWish(Long gameId, User user);

    List<WishDto> getWishList(Long userId);

    void moveToCart(Long gameId, User user);
}
