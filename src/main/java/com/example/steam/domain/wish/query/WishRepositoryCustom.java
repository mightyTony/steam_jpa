package com.example.steam.domain.wish.query;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.user.User;
import com.example.steam.domain.wish.dto.WishDto;

import java.util.List;

public interface WishRepositoryCustom {
    boolean existsWishByGameIdAndUser(Long gameId, User user);

    List<WishDto> findAllByUser_Id(Long userId);

    List<Long> findUsersByGame(Game game);
}
