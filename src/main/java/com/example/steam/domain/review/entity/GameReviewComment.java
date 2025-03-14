package com.example.steam.domain.review.entity;

import com.example.steam.domain.user.User;
import com.example.steam.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review_comment")
public class GameReviewComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private GameReview gameReview;

    // 대댓글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private GameReviewComment parentComment;

    // soft 삭제
    @Column(nullable = false)
    private boolean deleted = false;

    @Builder
    public GameReviewComment(String content, User user, GameReview gameReview, GameReviewComment parentComment) {
        this.content = content;
        this.user = user;
        this.gameReview = gameReview;
        this.parentComment = parentComment;
    }

    // 댓글 수정
    public void updateContent(String content) {
        this.content = content;
    }

    // 댓글 삭제
    public void delete() {
        this.deleted = true;
    }

    public String getDisplayContent() {
        return deleted ? "삭제된 댓글" : content;
    }
}
