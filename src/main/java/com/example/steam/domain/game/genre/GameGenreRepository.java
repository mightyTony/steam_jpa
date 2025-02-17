package com.example.steam.domain.game.genre;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameGenreRepository extends JpaRepository<GameGenre, Long> {
    Optional<GameGenre> findByGenreName(String genreName);
}
