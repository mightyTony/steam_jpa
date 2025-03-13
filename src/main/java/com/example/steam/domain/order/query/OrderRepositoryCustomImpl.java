package com.example.steam.domain.order.query;

import com.example.steam.domain.game.QGame;
import com.example.steam.domain.order.Order;
import com.example.steam.domain.order.QOrder;
import com.example.steam.domain.order.QOrderItem;
import com.example.steam.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QOrder order = QOrder.order;
    QOrderItem orderItem = QOrderItem.orderItem;
    QGame game = QGame.game;

    @Override
    public List<Order> findOrdersWithItemsByUser(User user) {
        return queryFactory
                .selectFrom(order)
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .leftJoin(orderItem.game, game).fetchJoin()
                .where(order.user.eq(user))
                .orderBy(order.createdAt.desc())
                .fetch();
    }
}
