package com.example.steam.domain.game.query;

import com.example.steam.domain.game.dto.GameDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GameRepositoryCustom {

    Page<GameDetailResponse> findGamesByCategory(String category, String name, Integer minPrice, Integer maxPrice, Pageable pageable);
}
