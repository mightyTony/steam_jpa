package com.example.steam.domain.game;

import com.example.steam.domain.game.genre.GameGenreRepository;
import com.example.steam.domain.game.query.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class GameServiceTest {
    @Autowired
    public GameRepository gameRepository;
    @Autowired
    public GameGenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        Game game = gameRepository.save(Game.builder()
                .name("Elden Ring")
                .developer("FromSoftware")
                .publisher("Bandai Namco")
                .content("압도적인 자유도를 가진 오픈월드 RPG")
                .price(59000)
                .releaseDate("2022-02-25")
                .pictureUrl("https://s3.amazonaws.com/test/eldenring.jpg")
                .build());
    }
}