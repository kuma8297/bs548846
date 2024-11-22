package com.bookstore.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private final String secret = "bookstore-secret-key"; // 加密密钥
    @Value("${jwt.expiration}")
    private long expiration; // 从配置文件中读取过期时间

    /**
     * 生成 JWT
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(subject)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + expiration))
                   .signWith(SignatureAlgorithm.HS256, secret)
                   .compact();
    }

    /**
     * 提取用户名
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * 检查是否过期
     */
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
}
