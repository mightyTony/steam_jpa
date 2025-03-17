package com.example.steam.domain.review.query;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.review.dto.GameReviewCreateResponse;
import com.example.steam.domain.review.dto.GameReviewReadResponse;
import com.example.steam.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameReviewRepositoryCustom {

    boolean findByUserAndGame(User user, Game game);

    GameReviewCreateResponse findReviewById(Long id);

    Page<GameReviewReadResponse> findGameReviewWithPaging(int page, int size, Pageable pageable, Long gameId, String sortBy);
}
