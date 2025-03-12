package com.example.steam.domain.mygame;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyGameRepository extends JpaRepository<MyGame, Long> {
    boolean existsByUserAndGame(User user, Game game);
}
