package com.example.steam.domain.game;

import com.example.steam.domain.game.genre.GameGenre;
import com.example.steam.domain.review.entity.GameReview;
import com.example.steam.util.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@ToString(exclude = {"reviews", "genres"})
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
    private int sales;
    private int discount; // 할인률
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean onSale; // 판매 중
    @Column(nullable = false)
    private String releaseDate;
    @Column(nullable = false)
    private int likeCount;
    @Column(nullable = false)
    private int dislikeCount;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GameGenre> genres = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GameReview> reviews;

    @Builder
    public Game(String name, String developer, String publisher, String content,
                int price, int totalPrice, String pictureUrl, boolean onSale,
                String releaseDate) {
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
        this.likeCount = 0;
        this.dislikeCount = 0;
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

    // todo 게임에 좋아요 구현 시 게임 리뷰에 좋아요/싫어요 값 변경 저장
    public void like() {
        this.likeCount ++;
    }

    // todo 게임에 좋아요 구현 시 게임 리뷰에 좋아요/싫어요 값 변경 저장
    public void dislike() {
        this.dislikeCount ++;
    }

    public void uploadImage(String imageUrl) {
        this.pictureUrl = imageUrl;
    }
}
