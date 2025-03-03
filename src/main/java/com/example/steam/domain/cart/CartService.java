package com.example.steam.domain.cart;

import com.example.steam.domain.cart.dto.CartViewResponse;
import com.example.steam.domain.user.User;

import java.util.List;

public interface CartService {

    /* 장바구니에 게임 추가
        @param : 게임 아이디, 유저 객체
     */
    void addToCart(Long gameId, User user);

    // 장바구니 목록 조회
    List<CartViewResponse> getCartItems(User user);

    // 장바구니에 해당 게임 삭제
    void deleteItemFromCart(User user, Long gameId);
}
