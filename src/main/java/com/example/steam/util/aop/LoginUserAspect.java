package com.example.steam.util.aop;

import com.example.steam.domain.user.User;
import com.example.steam.domain.user.UserRepository;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class LoginUserAspect {
    private final UserRepository userRepository;

    // @LoginUser 어노테이션 붙어 있고 메서드 파라미터로 user가 쓰일 떄 전에 이 AOP 실행
    @Before("@annotation(com.example.steam.util.annotation.LoginUser) && (args(..,user) || args(user,..))")
    public void validateUserExist(User user) {
//        log.info("[LOGIN AOP]");
//        log.info("[login aop] user : {}", user.toString());
        if(user == null || user.getId() == null) {
            throw new SteamException(ErrorCode.JWT_ILLEGAL_ARGUMENT);
        }

//        boolean isExist = userRepository.existsById(user.getId());
//        if(!isExist) {
//            throw new SteamException(ErrorCode.NOT_FOUND_USER_NAME);
//        }
    }
}
