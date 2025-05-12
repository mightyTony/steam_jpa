package com.example.steam.domain.wish;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "wish",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "game_Id"})}
)
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Builder
    public Wish(User user, Game game) {
        this.user = user;
        this.game = game;
    }
}
