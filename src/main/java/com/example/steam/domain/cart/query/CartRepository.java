package com.example.steam.domain.cart.query;

import com.example.steam.domain.cart.Cart;
import com.example.steam.domain.game.Game;
import com.example.steam.domain.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long>, CartRepositoryCustom {

    boolean existsByUserAndGame(User user, Game game);

    @EntityGraph(attributePaths = {"game"})
    List<Cart> findByUserId(Long id);

    Optional<Cart> findByUserAndGameId(User user, Long gameId);

    @EntityGraph(attributePaths = {"game"})
    List<Cart> findByUser(User user);

    void deleteByUserAndGameIdIn(User user, List<Long> list);
}
