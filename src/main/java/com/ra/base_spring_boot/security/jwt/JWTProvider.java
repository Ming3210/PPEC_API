package com.ra.base_spring_boot.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JWTProvider {

    @Value("${jwt.expire}")
    private long jwtExpire;

    @Value("${jwt.refresh}")
    private long jwtRefresh;

    @Value("${jwt.secret}")
    private String secretKeyString;

    private Key key;

    @jakarta.annotation.PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getEncoder().encode(secretKeyString.getBytes());
        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpire))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtRefresh))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("JWT token expired!");
        } catch (UnsupportedJwtException e) {
            log.error("JWT token unsupported!");
        } catch (MalformedJwtException e) {
            log.error("JWT token malformed!");
        } catch (SignatureException e) {
            log.error("JWT token signature error!");
        } catch (IllegalArgumentException e) {
            log.error("JWT token argument error!");
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
    public boolean isTokenNearExpiry(String token, long thresholdMillis) {
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getExpiration();
        long nowMillis = System.currentTimeMillis();
        return (expiration.getTime() - nowMillis) <= thresholdMillis;
    }
}
