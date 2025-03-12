package com.example.steam.domain.order.query;

import com.example.steam.domain.order.Order;
import com.example.steam.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
