package com.example.steam.domain.profile.dto;

import com.example.steam.domain.profile.comment.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private String writerNickname;
    private String writerUsername;
    private String writerProfileImageUrl;
    private String createdAt;
    private String updatedAt;

    public static CommentResponse toDto(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.id = comment.getId();
        response.content = comment.getContent();
        response.writerNickname = comment.getWriter().getNickname();
        response.writerUsername = comment.getWriter().getUsername();
        response.writerProfileImageUrl = comment.getWriter().getProfileImageUrl();
        response.createdAt = comment.getCreatedAt().toString();
        response.updatedAt = comment.getLastModifiedAt().toString();

        return response;
    }

    @QueryProjection
    public CommentResponse(Long id, String content, String writerNickname, String writerUsername, String writerProfileImageUrl, String createdAt, String updatedAt) {
        this.id = id;
        this.content = content;
        this.writerNickname = writerNickname;
        this.writerUsername = writerUsername;
        this.writerProfileImageUrl = writerProfileImageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
