package com.example.steam.domain.profile.query;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.profile.mygame.MyGame;
import com.example.steam.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyGameRepository extends JpaRepository<MyGame, Long>, MyGameRepositoryCustom {
    boolean existsByUserAndGame(User user, Game game);
}
