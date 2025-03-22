package com.example.steam.config.jwt;

import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public String getRoleFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody().get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }
        catch (SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT - {}", e.getMessage());
            log.warn("EXCEPTED TOKEN : {}", token);
            throw new SteamException(ErrorCode.JWT_MALFORMED);
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT - {}", e.getMessage());
            log.warn("EXCEPTED TOKEN : {}", token);
            throw new SteamException(ErrorCode.JWT_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT - {}", e.getMessage());
            log.warn("EXCEPTED TOKEN : {}", token);
            throw new SteamException(ErrorCode.JWT_UNSUPPORTED);
        } catch (IllegalArgumentException e) {
            log.warn("JWT claims string is empty - {}", e.getMessage());
            log.warn("EXCEPTED TOKEN : {}", token);
            throw new SteamException(ErrorCode.JWT_ILLEGAL_ARGUMENT);
        }
        //return false;
    }
}
