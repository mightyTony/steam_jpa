package com.example.steam.domain.profile.dto;

import com.example.steam.domain.profile.comment.Comment;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@Schema(description = "프로필 댓글 응답 DTO")
public class CommentResponse {
    @Schema(description = "프로필 댓글 id", example = "1")
    private Long id;
    @Schema(description = "댓글 내용", example = "hello")
    private String content;
    @Schema(description = "댓글 작성자 닉네임", example = "토니")
    private String writerNickname;
    @Schema(description = "댓글 작성자 아이디", example = "tony")
    private String writerUsername;
    @Schema(description = "댓글 작성자 프로필 이미지 경로", example = "주소/~123.com")
    private String writerProfileImageUrl;


    @Schema(description = "작성일", example = "2024-01-01T12:00:00")
    private String createdAt;

    @Schema(description = "수정일", example = "2024-01-02T14:00:00")
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
