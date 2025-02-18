package com.example.steam.domain.game;

import com.example.steam.domain.game.genre.GameGenre;
import com.example.steam.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@ToString
@Table(name = "game")
public class Game extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String developer;

    @Column(nullable = false, length = 50)
    private String publisher;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int price;
    @Column(nullable = false, name = "total_price")
    private int totalPrice; // 가격 % 할인률
    private String pictureUrl;
    private int sales = 0;
    private int discount;
    private boolean onSale; // 판매 중

    @Column(nullable = false)
    private String releaseDate;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GameGenre> genres = new ArrayList<>();

    @Builder
    public Game(String name, String developer, String publisher, String content, int price, int totalPrice, String pictureUrl, boolean onSale, String releaseDate) {
        this.name = name;
        this.developer = developer;
        this.publisher = publisher;
        this.content = content;
        this.price = price;
        this.totalPrice = totalPrice;
        this.pictureUrl = pictureUrl;
        this.sales = 0;
        this.discount = 0;
        this.onSale = onSale;
        this.releaseDate = releaseDate;
    }

    public void discount(int discount) {
        this.discount = discount;
        this.totalPrice = (int)Math.ceil(price * (1 - discount / 100.0));
    }
    public void publish() {
        this.onSale = true;
    }

    public void update(String name, String developer, String publisher,
                       String content, int price, String pictureUrl) {
        this.name = name;
        this.developer = developer;
        this.publisher = publisher;
        this.content = content;
        this.price = price;
        this.totalPrice = price; // 가격 변경 시 같이 변경
        this.pictureUrl = pictureUrl;
    }
}
