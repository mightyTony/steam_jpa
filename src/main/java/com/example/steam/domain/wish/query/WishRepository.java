package com.example.steam.domain.wish.query;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.user.User;
import com.example.steam.domain.wish.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long>, WishRepositoryCustom {
    Optional<Wish> findByUserAndGame(User user, Game game);



    //Optional<Void> getWishByGameId(Long gameId);

    /*
    findByUserIdAndGameId : 특정 유저가 특정 게임을 찜했는지 확인

    findAllByUserId : 유저의 찜 목록 조회

    findAllByGameId : 특정 게임을 찜한 유저 목록 조회 (알림에 필요)

    existsByUserIdAndGameId : 이미 찜했는지 빠른 체크

    deleteByUserIdAndGameId : 찜 삭제
     */
}
