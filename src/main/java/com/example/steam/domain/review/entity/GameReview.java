package com.example.steam.domain.review.entity;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.user.User;
import com.example.steam.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "game_id" })
}) // 한 게임당 리뷰 하나
public class GameReview extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 리뷰 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 리뷰 게임
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    // 추천, 비추천
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewType reviewType;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "gameReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameReviewComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "gameReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameReviewLike> likes = new ArrayList<>();

    // 리뷰 수정
    public void update(ReviewType reviewType, String content) {
        this.reviewType = reviewType;
        this.content = content;
    }

    // 리뷰 삭제
    public void delete() {
        this.deleted = true;
    }

    @Builder
    public GameReview(User user, Game game, ReviewType reviewType, String content, boolean deleted) {
        this.user = user;
        this.game = game;
        this.reviewType = reviewType;
        this.content = content;
        this.deleted = deleted;
    }
}
