package com.example.steam.domain.cart;

import com.example.steam.domain.cart.dto.CartViewResponse;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/cart")
@Tag(name = "장바구니", description = "장바구니 API (추가, 조회, 삭제)")
//@LoginUser
public class CartController {
    private final CartService cartService;

    // 장바구니 추가
    @Operation(summary = "장바구니에 게임 추가", description = "선택한 게임을 장바구니에 추가합니다.")
    @LoginUser
    @PostMapping()
    public Response<Void> addToCart(@AuthenticationPrincipal User user,
                                    @RequestParam("game") Long gameId) {
        cartService.addToCart(gameId, user);
        return Response.success();
    }

    // 장바구니 목록 조회
    @Operation(summary = "장바구니 목록 조회", description = "로그인한 사용자의 장바구니 목록을 조회합니다.")
    @LoginUser
    @GetMapping
    public Response<List<CartViewResponse>> getCartItems(@AuthenticationPrincipal User user) {
        List<CartViewResponse> result = cartService.getCartItems(user);
        return Response.success(result);
    }

    // 장바구니에서 삭제
    @Operation(summary = "장바구니에서 게임 삭제", description = "장바구니에서 특정 게임을 제거합니다.")
    @LoginUser
    @DeleteMapping("/{game}")
    public Response<Void> deleteCartItem(@PathVariable("game") Long gameId, @AuthenticationPrincipal User user) {
        cartService.deleteItemFromCart(user, gameId);
        return Response.success();
    }
}
