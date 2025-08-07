package com.example.steam.config.jwt;

import com.example.steam.config.CustomUserDetailsService;
import com.example.steam.domain.user.Role;
import com.example.steam.domain.user.User;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private Key key;
    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @PostConstruct
    protected void init() {
        byte[] decodedkey = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(decodedkey);
    }

//    public JwtTokenProvider(CustomUserDetailsService customUserDetailsService) {
//        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
//        this.customUserDetailsService = customUserDetailsService;
//    }

    public String createToken(Authentication authentication) {
//        log.info("[createToken] key - {}", key.toString());
        User user = (User) authentication.getPrincipal();
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("userId", user.getId());
        claims.put("role", user.getRole().name());
        claims.put("nickname", user.getNickname());

        Date now = new Date();
        long ACCESS_TOKEN_EXPIRATION_TIME = 1000 * 60 * 60;
        Date expiration = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION_TIME);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }


    // 토큰 유효, 만료 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }
        catch (SecurityException | MalformedJwtException e) {
            //log.warn("Invalid JWT - {}", e.getMessage());
            //log.warn("EXCEPTED TOKEN : {}", token);
            throw new SteamException(ErrorCode.JWT_MALFORMED);
        } catch (ExpiredJwtException e) {
            //log.warn("Expired JWT - {}", e.getMessage());
            //log.warn("EXCEPTED TOKEN : {}", token);
            throw new SteamException(ErrorCode.JWT_EXPIRED);
        } catch (UnsupportedJwtException e) {
            //log.warn("Unsupported JWT - {}", e.getMessage());
            //log.warn("EXCEPTED TOKEN : {}", token);
            throw new SteamException(ErrorCode.JWT_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            //log.warn("JWT claims string is empty - {}", e.getMessage());
            //log.warn("EXCEPTED TOKEN : {}", token);
            throw new SteamException(ErrorCode.JWT_ILLEGAL_ARGUMENT);
        }
        //return false;
    }

    // jwt 토큰에서 정보 추출
    public Authentication getAuthentication(String accessToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(accessToken)
                .getBody();

        String username = claims.getSubject();
        String role = claims.get("role",String.class);
        Long userId = claims.get("userId", Long.class);
        String nickname = claims.get("nickname", String.class);

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        User user = User.builder()
                .username(username)
                .role(Role.valueOf(role))
                .build();

        user.getIdAndNickname(userId,nickname);

        return new UsernamePasswordAuthenticationToken(user,null, authorities);
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public Long getUserId(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("userId", Long.class);
        } catch (JwtException | IllegalArgumentException e) {
            log.error("토큰에서 userId 추출 실패", e);
            throw new SteamException(ErrorCode.JWT_ILLEGAL_ARGUMENT);
        }
    }


}
