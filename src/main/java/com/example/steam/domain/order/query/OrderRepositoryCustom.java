package com.example.steam.domain.order.query;

import com.example.steam.domain.order.Order;
import com.example.steam.domain.user.User;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> findOrdersWithItemsByUser(User user);
}
