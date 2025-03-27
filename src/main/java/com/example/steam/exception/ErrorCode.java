package com.example.steam.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //Auth
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "이미 가입된 아이디 입니다."),
    DUPLICATED_USER_NAME_OR_EMAIL(HttpStatus.CONFLICT, "이미 가입된 아이디 혹은 이메일 입니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러"),
    NOT_FOUND_USER_NAME(HttpStatus.BAD_REQUEST, "존재 하지 않은 아이디 입니다" ),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호를 틀리셨습니다" ),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "어드민 권한 없음" ),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다"),

    // Game
    ALREADY_EXISTED_GAME(HttpStatus.CONFLICT, "이미 존재하는 게임 입니다."),
    NOT_FOUND_GAME(HttpStatus.BAD_REQUEST, "존재 하지 않는 게임 입니다." ),

    // Cart
    ALREADY_IN_CART(HttpStatus.CONFLICT, "장바구니에 이미 있습니다."),
    NOT_FOUND_GAME_IN_CART(HttpStatus.CONFLICT, "장바구니에 해당 게임이 존재하지 않습니다." ),

    // Order
    NOT_FOUND_ORDER_TID(HttpStatus.CONFLICT, "카카오 구매건 조회가 되지않습니다."),
    ORDER_NOT_FOUND(HttpStatus.CONFLICT, "구매 조회 안됨" ),
    ORDER_FAILED(HttpStatus.BAD_REQUEST, "진행중인 거래가 있습니다. 잠시 후 다시 시도해 주세요." ),
    ALREADY_BUY_GAME(HttpStatus.BAD_REQUEST, "이미 구매한 게임이 포함되어 있습니다"),

    // jwt
    JWT_MALFORMED(HttpStatus.UNAUTHORIZED, "잘못된 JWT 형식입니다."),
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "JWT 토큰이 만료되었습니다."),
    JWT_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "지원하지 않는 JWT 토큰입니다."),
    JWT_ILLEGAL_ARGUMENT(HttpStatus.UNAUTHORIZED, "JWT 토큰이 비어있거나 잘못된 값입니다."),
    JWT_IS_NULL(HttpStatus.UNAUTHORIZED, "토큰이 비어있습니다." ),

    // review
    REVIEW_ALREADY_WRITE(HttpStatus.BAD_REQUEST, "이미 이 게임에 대한 리뷰를 작성 했습니다." ),
    NOT_FOUND_GAME_REVIEW(HttpStatus.BAD_REQUEST, "게임 리뷰가 존재 하지 않습니다." ),

    // friend
    ALREADY_FRIENDSHIP(HttpStatus.BAD_REQUEST,"이미 친구 관계 이거나 요청을 보냈습니다"),
    ILLEGAL_FRIENDSHIP(HttpStatus.BAD_REQUEST, "자신에게 친구 보냈습니다"),
    ILLEGAL_FRIENDSHIP_REQUEST(HttpStatus.BAD_REQUEST, "친구 요청이 잘못 됐습니다." ),
    NOT_FOUND_FRIENDSHIP(HttpStatus.CONFLICT,"친구 관계 데이터를 찾을 수 없습니다" ),
    NOT_FOUND_MY_FRIENDSHIP(HttpStatus.CONFLICT,"아직 친구가 없습니다." ),

    // profile
    NOT_FOUND_PROFILE(HttpStatus.CONFLICT, "프로필을 찾을 수 없습니다" ),
    NOT_FOUND_COMMENT(HttpStatus.CONFLICT, "프로필 내 댓글을 찾을 수 없습니다" ),

    // FILE
    NOT_FOUND_IMAGE_FILE(HttpStatus.BAD_REQUEST, "이미지 파일이 없습니다." ),
    UPLOAD_FAIL(HttpStatus.CONFLICT,"S3 업로드 실패" ),
    ILLEGAL_ARGUMENT_MULTIPARTFILE(HttpStatus.BAD_REQUEST, "이미지 파일 에러" ),;



    private final HttpStatus status;
    private final String message;
}
