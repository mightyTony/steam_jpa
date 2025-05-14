package com.example.steam.domain.notification.event;

import com.example.steam.domain.game.Game;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameSaleEvent {
    private Game game;
}
