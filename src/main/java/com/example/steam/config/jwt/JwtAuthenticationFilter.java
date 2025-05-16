package com.example.steam.config.jwt;

import com.example.steam.config.CustomUserDetailsService;
import com.example.steam.exception.SteamException;
import com.example.steam.util.annotation.AdminAuthorize;
import com.example.steam.util.annotation.LoginUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
//    private final CustomUserDetailsService userDetailsService;
    private final ApplicationContext applicationContext;
    private final JwtExceptionHandler jwtExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            HandlerMethod handlerMethod = getHandlerMethod(request);

            if (requiresAuthentication(handlerMethod)) {
                String token = resolveToken(request);

                if (token != null && jwtTokenProvider.validateToken(token)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            filterChain.doFilter(request, response);
        } catch (SteamException e) {
            jwtExceptionHandler.commence(request, response, new InsufficientAuthenticationException(e.getMessage()));
        }

    }


    private boolean requiresAuthentication(HandlerMethod method) {
        if (method == null) return false;

        return method.hasMethodAnnotation(AdminAuthorize.class)
                || method.hasMethodAnnotation(LoginUser.class);
    }

    private HandlerMethod getHandlerMethod(HttpServletRequest request) {
        try {
            HandlerExecutionChain handler = applicationContext
                    .getBean(RequestMappingHandlerMapping.class)
                    .getHandler(request);
            if (handler != null && handler.getHandler() instanceof HandlerMethod hm) {
                return hm;
            }
        } catch (Exception e) {
            log.warn("[LOG] HandlerMethod 추출 실패: {}", e.getMessage());
        }
        return null;
    }
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}



//        // 토큰 가져오기
//        String accessToken = resolveToken(request);
//
//        // 접근 토큰 유효 시 검증
//        if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
//            // 토큰 정보로 Authentication 객체 생성
//            Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
//
//            // Context authentication
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        }
//        filterChain.doFilter(request, response);