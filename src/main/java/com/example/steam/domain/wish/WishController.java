package com.example.steam.domain.wish;

import com.example.steam.domain.user.User;
import com.example.steam.domain.wish.dto.WishDto;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/wish")
@Slf4j
@RequiredArgsConstructor
@RestController
public class WishController {
    private final WishService wishService;

    // 1. (post) 찜 생성
    @LoginUser
    @PostMapping
    public Response<Void> addToWish(@RequestParam("game") Long gameId, @AuthenticationPrincipal User user) {

        wishService.addToWish(gameId, user);

        return Response.success();
    }

    // 찜 삭제
    @LoginUser
    @DeleteMapping()
    public Response<Void> removeWish(@RequestParam("game") Long gameId, @AuthenticationPrincipal User user) {
        wishService.removeWish(gameId, user);

        return Response.success();
    }

    // 찜 전체 조회
    @GetMapping("/user/{userId}")
//    @LoginUser
    public Response<List<WishDto>> getWishList(@PathVariable("userId") Long userId) {
        List<WishDto> wishes = wishService.getWishList(userId);

        return Response.success(wishes);
    }

    // 찜 -> 장바구니    찜 목록에 있는 것 장바구니에 추가 -> 찜 목록에서 삭제
    @PatchMapping("/move/{game}")
    @LoginUser
    public Response<Void> moveToCart(@PathVariable("game") Long gameId, @AuthenticationPrincipal User user) {
        wishService.moveToCart(gameId, user);

        return Response.success();
    }
}
