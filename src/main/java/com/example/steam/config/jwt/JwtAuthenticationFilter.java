package com.example.steam.config.jwt;

import com.example.steam.exception.SteamException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationContext applicationContext;
    private final JwtExceptionHandler jwtExceptionHandler;

    private static final List<String> BLOCK_PATTERNS = List.of(
            "/.git", "/.git/**", "/.env", "/.aws/**", "/.DS_Store",
            "/**/*.php", "/index.php", "/**/eval-stdin.php", "/**/think/app/invokefunction",
            "/cms/**", "/crm/**", "/admin/**", "/panel/**", "/webui/**",
            "/geoserver/**", "/drupal/**", "/wordpress/**", "/joomla/**", "/typo3/**",
            "/api/vendor/**", "/auth/x.js", "/owa/**", "/aaa", "/aab",
            "/robots.txt", "/sitemap.xml", "/hello.world",
            "/containers/**", "/test_**", "/tests/**", "/testing/**", "/demo/**",
            "/apps/**", "/app/**", "/lib/**", "/ws/**", "/V2/**", "/backup/**", "/blog/**", "/public/**",
            "/favicon.ico", "/favicon-16x16.png", "/favicon-32x32.png", "/service-worker.js"
    );
    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();

        for (String pattern : BLOCK_PATTERNS) {
            if (pathMatcher.match(pattern, uri)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }

        try {
            String token = resolveToken(request);
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);
        } catch (SteamException e) {
            jwtExceptionHandler.commence(request, response, new InsufficientAuthenticationException(e.getMessage()));
        }
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