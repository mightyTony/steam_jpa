package com.example.steam.domain.profile.query;

import com.example.steam.domain.profile.QProfile;
import com.example.steam.domain.profile.comment.QComment;
import com.example.steam.domain.profile.dto.CommentResponse;
import com.example.steam.domain.profile.dto.QCommentResponse;
import com.example.steam.domain.user.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    QComment comment = QComment.comment;
    QProfile profile = QProfile.profile;
    QUser user = QUser.user;


    @Override
    public Page<CommentResponse> findCommentsWithPaging(int page, int size, Pageable pageable, Long userId) {
        List<CommentResponse> lists = queryFactory
                .select(new QCommentResponse(
                        comment.id,
                        comment.content,
                        comment.writer.nickname,
                        comment.writer.username,
                        user.profileImageUrl,
                        comment.createdAt.stringValue(),
                        comment.lastModifiedAt.stringValue()
                ))
                .from(comment)
//                .leftJoin(comment.profile, profile)
                .leftJoin(comment.writer, user)
                .where(comment.profile.id.eq(userId))
                .orderBy(comment.createdAt.desc())
                .offset(page)
                .limit(size)
                .fetch();

        long total = Optional.ofNullable(queryFactory
                .select(comment.count())
                .from(comment)
                .where(comment.profile.id.eq(userId))
                .fetchOne())
                .orElse(0L);

        return new PageImpl<>(lists, pageable, total);
    }
}
