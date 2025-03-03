package com.example.steam.domain.game.genre;

import com.example.steam.domain.game.Game;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game_genre")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "game")
public class GameGenre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;

    @Column(name = "genre_name")
    private String genreName;

}
