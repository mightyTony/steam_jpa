package com.example.steam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "이미 가입된 아이디 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러"),
    NOT_FOUND_USER_NAME(HttpStatus.BAD_REQUEST, "존재 하지 않은 아이디 입니다" ),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호를 틀리셨습니다" ),

    ALREADY_EXISTED_GAME(HttpStatus.CONFLICT, "이미 존재하는 게임 입니다."),
    NOT_FOUND_GAME(HttpStatus.BAD_REQUEST, "존재 하지 않는 게임 입니다." ),

    ALREADY_IN_CART(HttpStatus.CONFLICT, "장바구니에 이미 있습니다."),
    NOT_FOUND_GAME_IN_CART(HttpStatus.CONFLICT, "장바구니에 해당 게임이 존재하지 않습니다." ),

    NOT_FOUND_ORDER_TID(HttpStatus.CONFLICT, "카카오 구매건 조회가 되지않습니다."),
    ORDER_NOT_FOUND(HttpStatus.CONFLICT, "구매 조회 안됨" );
    private final HttpStatus status;
    private final String message;
}
