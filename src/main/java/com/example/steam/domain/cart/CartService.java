package com.example.steam.domain.cart;

import com.example.steam.domain.cart.query.CartRepository;
import com.example.steam.domain.game.Game;
import com.example.steam.domain.game.query.GameRepository;
import com.example.steam.domain.user.User;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final GameRepository gameRepository;
    @Transactional
    public void addToCart(Long gameId, User user) {
        // 게임 조회
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME));

        // 카트에 이미 있는지 중복 검사
        if(cartRepository.existsByUserAndGame(user, game)) {
            throw new SteamException(ErrorCode.NOT_FOUND_GAME_IN_CART);
        }

        // 장바구니에 추가
        Cart cart = Cart.builder()
                .user(user)
                .game(game)
                .build();

        cartRepository.save(cart);
    }
}
