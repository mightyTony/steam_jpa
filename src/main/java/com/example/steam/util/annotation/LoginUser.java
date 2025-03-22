package com.example.steam.util.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ADMIN','USER') and isAuthenticated()")
// fixme :: token null 일시 NPE 터진다. aop로 검증 로직 짜야함.
public @interface LoginUser {
}
