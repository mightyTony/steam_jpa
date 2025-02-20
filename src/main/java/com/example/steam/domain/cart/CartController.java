package com.example.steam.domain.cart;

import com.example.steam.domain.game.dto.GameResponse;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/cart")
@LoginUser
public class CartController {
    private final CartService cartService;

    // 장바구니 추가
    @PostMapping()
    public Response<Void> addToCart(@RequestParam("game") Long gameId, @AuthenticationPrincipal User user) {
        cartService.addToCart(gameId, user);
        log.info("유저 = {} ", user.getUsername());
        return Response.success();
    }

//    // 장바구니 목록 조회
//    @GetMapping
//    public Response<List<CartResponse>> getCartItems(@AuthenticationPrincipal User user) {
//        List<CartResponse> result = cartService.getCartItems(user);
//
//        return Response.success(result);
//    }
//
//    // 장바구니에서 삭제
//    @DeleteMapping("/{id}")
//    public Response<Void> deleteCartItem(@RequestParam("id") Long gameId, @AuthenticationPrincipal User user) {
//        cartService.deleteItemFromCart(gameId);
//
//        return Response.success();
//    }
}
