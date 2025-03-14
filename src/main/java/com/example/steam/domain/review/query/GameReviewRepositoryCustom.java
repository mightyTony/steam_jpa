package com.example.steam.domain.review.query;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.review.dto.GameReviewCreateResponse;
import com.example.steam.domain.user.User;

public interface GameReviewRepositoryCustom {

    boolean findByUserAndGame(User user, Game game);

    GameReviewCreateResponse findReviewById(Long id);
}
