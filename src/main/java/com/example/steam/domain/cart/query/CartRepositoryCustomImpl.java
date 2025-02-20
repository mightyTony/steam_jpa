package com.example.steam.domain.cart.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRepositoryCustomImpl implements CartRepositoryCustom {
    private final JPAQueryFactory queryFactory;
}
