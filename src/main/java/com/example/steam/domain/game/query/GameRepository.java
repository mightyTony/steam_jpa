package com.example.steam.domain.game.query;

import com.example.steam.domain.game.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long>, GameRepositoryCustom{

//    @Query( "SELECT g FROM Game g LEFT JOIN g.genres genre " +
//            "WHERE (:keyword IS NULL OR g.name LIKE %:keyword%) " +
//            "AND (:genre is NULL OR genre.genreName = :genre)"
//    )
//    Page<Game> searchGames(@Param("keyword") String keyword, @Param("genre") String genre, Pageable pageable);

    Optional<Game> findByName(String name);
}
