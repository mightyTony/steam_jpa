package com.example.steam.domain.profile.dto;

import com.example.steam.domain.profile.comment.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentResponse {
    private Long id;
    private String content;
    private String writerNickname;
    private String writerUsername;
    private String createdAt;
    private String updatedAt;

    public static CommentResponse toDto(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.id = comment.getId();
        response.content = comment.getContent();
        response.writerNickname = comment.getWriter().getNickname();
        response.writerUsername = comment.getWriter().getUsername();
        response.createdAt = comment.getCreatedAt().toString();
        response.updatedAt = comment.getLastModifiedAt().toString();

        return response;
    }
}
