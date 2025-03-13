package com.example.steam.domain.cart;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;

@Entity
@Table(name = "cart", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id","game_id"})
})
@Getter
@NoArgsConstructor
public class Cart {
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
    public Cart(User user, Game game) {
        this.user = user;
        this.game = game;
    }
}
