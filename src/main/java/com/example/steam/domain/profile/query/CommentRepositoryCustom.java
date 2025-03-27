package com.example.steam.domain.profile.query;

import com.example.steam.domain.profile.dto.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<CommentResponse> findCommentsWithPaging(int page, int size, Pageable pageable, Long profileId);
}
