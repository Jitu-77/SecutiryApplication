package com.jp.SecutiryApp.SecutiryApplication.service;

import com.jp.SecutiryApp.SecutiryApplication.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
@Service
public class JwtService {
    @Value("${jwt.secretKey}")
    private String jwtSecretKey;
    private SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserEntity user){
        return Jwts
                // for generation we use builder and for parsing we use parser
                .builder()
                //subject or the user id or uniqueness
                .subject(user.getId().toString())
                //via claim we can pass values as key:value
                .claim("email", user.getEmail())
                //.claim("roles", Set.of("ADMIN", "USER")) // before role based
                .claim("roles", user.getRoles().toString()) // before role based
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*2))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(UserEntity user){
        return Jwts
                .builder()
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10))
                .signWith(getSecretKey())
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                //validate the token
                .parseSignedClaims(token)
                //payload contains ll the things
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }
}
