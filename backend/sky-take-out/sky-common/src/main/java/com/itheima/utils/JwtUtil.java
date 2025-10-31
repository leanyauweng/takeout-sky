package com.itheima.utils;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * 生成 JWT
     * @param secretKey 密钥
     * @param ttl 有效期（毫秒）
     * @param claims 自定义 payload
     * @return JWT 字符串
     */
    public static String createJWT(String secretKey, long ttl, Map<String, Object> claims) {
        // 使用 HS256 算法加密
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + ttl);

        return Jwts.builder()
                .setClaims(claims)     // 自定义 payload
                .setIssuedAt(now)       // 签发时间
                .setExpiration(exp)     // 过期时间
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析 JWT
     * @param token JWT 字符串
     * @param secretKey 密钥
     * @return Claims
     */
    public static Claims parseJWT(String secretKey, String token) throws JwtException {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
