package com.example.steam.domain.wish;

import com.example.steam.domain.cart.Cart;
import com.example.steam.domain.cart.query.CartRepository;
import com.example.steam.domain.game.Game;
import com.example.steam.domain.game.query.GameRepository;
import com.example.steam.domain.user.User;
import com.example.steam.domain.wish.dto.WishDto;
import com.example.steam.domain.wish.query.WishRepository;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishServiceImpl implements WishService {
    private final CartRepository cartRepository;

    private final WishRepository wishRepository;
    private final GameRepository gameRepository;

    @Transactional
    public void addToWish(Long gameId, User user) {
        // 게임 조회
        Game game = gameRepository.findById(gameId)
                .orElseThrow(()-> new SteamException(ErrorCode.NOT_FOUND_GAME));

        log.info("game: {}, user : {}", gameId, user.getId());
        // 이미 찜에 있는지
        if(wishRepository.existsWishByGameIdAndUser(gameId, user)){
            throw new SteamException(ErrorCode.ALREADY_IN_WISH);
        }

        // 찜에 넣기
        Wish wish = Wish.builder()
                .user(user)
                .game(game)
                .build();

        wishRepository.save(wish);

        log.info("[찜 추가 - user : {}, game : {}", user.getUsername(), game.getName());
    }

    @Override
    @Transactional
    public void removeWish(Long gameId, User user) {
        // 1. 게임 조회
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME));
        // 2. 찜 조회
        Wish wish = wishRepository.findByUserAndGame(user, game)
                .orElseThrow(()-> new SteamException(ErrorCode.NOT_FOUND_WISH));
        // 3. 삭제
        wishRepository.delete(wish);

        log.info("[찜 삭제 - user : {}, gameId : {}", user.getUsername(), game.getName());
    }

    @Override
//    @Transactional(readOnly = true)
    public List<WishDto> getWishList(Long userId) {
        List<WishDto> wishes = wishRepository.findAllByUser_Id(userId);

        if(wishes.isEmpty()){
            throw new SteamException(ErrorCode.NOT_FOUND_WISH);
        }

        return wishes;
    }

    // 찜 -> 장바구니, 찜 목록에 있는 것 장바구니에 추가 -> 찜 목록에서 삭제  3번 이용
    @Override
    @Transactional
    public void moveToCart(Long gameId, User user) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME));

        Wish wish = wishRepository.findByUserAndGame(user, game)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_WISH));

        Cart cart = Cart.builder()
                .game(game)
                .user(user)
                .build();

        cartRepository.save(cart);
        wishRepository.delete(wish);

        log.info("[찜 - 장바구니 이동] - user : {}, game : {}", user.getUsername(), game.getName());
    }

}
