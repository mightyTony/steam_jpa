package com.example.steam.domain.review.entity;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.user.User;
import com.example.steam.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "game_id" }) // 게임당 유저는 리뷰를 한개만 쓸 수 있게 유니크 설정
})
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
    @Column(nullable = false)
    private Boolean recommend;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "gameReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameReviewComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "gameReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameReviewLike> likes = new ArrayList<>();

    // 리뷰 수정
    public void update(boolean recommend, String content) {
        this.recommend = recommend;
        this.content = content;
    }

    // 리뷰 삭제
    public void delete() {
        this.deleted = true;
    }

    @Builder
    public GameReview(User user, Game game, boolean recommend, String content, boolean deleted) {
        this.user = user;
        this.game = game;
        this.recommend = recommend;
        this.content = content;
        this.deleted = deleted;
    }
}
