package com.example.steam.domain.cart;

import com.example.steam.domain.cart.dto.CartViewResponse;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        return Response.success();
    }

    // 장바구니 목록 조회
    @GetMapping
    public Response<List<CartViewResponse>> getCartItems(@AuthenticationPrincipal User user) {
        log.info("[장바구니 목록 조회]");
        List<CartViewResponse> result = cartService.getCartItems(user);
        log.info("[장바구니 목록 조회] result : {}", result);
        return Response.success(result);
    }

    // 장바구니에서 삭제
    @DeleteMapping("/{game}")
    public Response<Void> deleteCartItem(@PathVariable("game") Long gameId, @AuthenticationPrincipal User user) {

        cartService.deleteItemFromCart(user, gameId);

        return Response.success();
    }
}
