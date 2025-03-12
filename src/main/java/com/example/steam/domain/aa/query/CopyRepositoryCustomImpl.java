package com.example.steam.domain.aa.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CopyRepositoryCustomImpl implements CopyRepositoryCustom{
    private final JPAQueryFactory queryFactory;
}
