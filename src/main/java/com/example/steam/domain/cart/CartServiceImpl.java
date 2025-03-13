package com.example.steam.domain.cart;

import com.example.steam.domain.cart.dto.CartViewResponse;
import com.example.steam.domain.cart.query.CartRepository;
import com.example.steam.domain.game.Game;
import com.example.steam.domain.game.query.GameRepository;
import com.example.steam.domain.mygame.MyGameRepository;
import com.example.steam.domain.user.User;
import com.example.steam.domain.user.UserRepository;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final MyGameRepository myGameRepository;
    @Transactional
    public void addToCart(Long gameId, User user) {
        // 게임 조회
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME));

        // 카트에 이미 있는지 중복 검사
        if(cartRepository.existsByUserAndGame(user, game)) {
            throw new SteamException(ErrorCode.ALREADY_IN_CART);
        }

        // 마이 게임에 이미 있는지 중복 검사
        if(myGameRepository.existsByUserAndGame(user,game)) {
            throw new SteamException(ErrorCode.ALREADY_BUY_GAME);
        }

        // 장바구니에 추가
        Cart cart = Cart.builder()
                .user(user)
                .game(game)
                .build();

        cartRepository.save(cart);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartViewResponse> getCartItems(User user) {
        List<Cart> Items = cartRepository.findByUserId(user.getId());

        return Items.stream()
                .map(CartViewResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteItemFromCart(User user, Long gameId) {
        Cart cartItem = cartRepository.findByUserAndGameId(user, gameId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME_IN_CART));

        cartRepository.delete(cartItem);
    }

}
