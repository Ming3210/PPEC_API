package com.ra.base_spring_boot.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
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

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());
        log.info("Generated JWT Secret Key (Base64): {}", encodedKey);
    }

    public String generateToken(String username){
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpire))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){
            log.error("JWT token expired!");
        }catch (UnsupportedJwtException e){
            log.error("JWT token unsupported!");
        }catch (MalformedJwtException e){
            log.error("JWT token malformed!");
        }catch (SignatureException e){
            log.error("JWT token signature error!");
        }catch (IllegalArgumentException e){
            log.error("JWT token argument error!");
        }
        return false;
    }

    public String getUsernameFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String refreshToken(String token, String username){
        if(validateToken(token) && getUsernameFromToken(token).equals(username)){
            Date now = new Date();
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + jwtRefresh))
                    .signWith(key)
                    .compact();
        }
        return null;
    }
}
