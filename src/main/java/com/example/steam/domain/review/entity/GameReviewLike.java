package com.example.steam.domain.review.entity;

import com.example.steam.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_like", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "review_id"})
}) // 좋아요는 한번만
public class GameReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private GameReview gameReview;

    @Builder
    public GameReviewLike(User user, GameReview gameReview) {
        this.user = user;
        this.gameReview = gameReview;
    }
}
