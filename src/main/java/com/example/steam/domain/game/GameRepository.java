package com.example.steam.domain.game;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long>{

    @Query( "SELECT g FROM Game g LEFT JOIN g.genres genre " +
            "WHERE (:keyword IS NULL OR g.name LIKE %:keyword%) " +
            "AND (:genre is NULL OR genre.genreName = :genre)"
    )
    Page<Game> searchGames(@Param("keyword") String keyword, @Param("genre") String genre, Pageable pageable);

    Optional<Game> findByName(String name);
}
