package com.example.steam.domain.game.query;

import com.example.steam.domain.game.dto.GameDetailResponse;
import com.example.steam.domain.game.dto.GameRankingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;

import java.time.LocalDateTime;
import java.util.List;

public interface GameRepositoryCustom {

    @EntityGraph(attributePaths = {"genres"})
    Page<GameDetailResponse> findGamesByCategory(String category, String name, Integer minPrice, Integer maxPrice, int page, int size);

    List<GameRankingResponse> findTopGamesBySales(LocalDateTime since);

    List<GameRankingResponse> findTopGames();
}
