package com.example.steam.util.aop;

import com.example.steam.domain.user.Role;
import com.example.steam.domain.user.User;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class AdminUserAspect {

    @Before("@annotation(com.example.steam.util.annotation.AdminAuthorize) && (args(..,user) || args(user,..))")
    public void validateIsAdmin(User user) {
//        log.info("[ADMIN AOP]");
        if (user == null) {
            throw new SteamException(ErrorCode.UNAUTHORIZED_ACCESS); // user == null 방어
        }

        if (user.getRole() != Role.ADMIN) {
            throw new SteamException(ErrorCode.UNAUTHORIZED_ACCESS);
        }
        if (user.getId() == null) {
            throw new SteamException(ErrorCode.JWT_ILLEGAL_ARGUMENT);
        }
    }
}
