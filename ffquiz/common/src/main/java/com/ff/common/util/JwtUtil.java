package com.ff.common.util;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ff.common.entity.dto.SessionUserDto;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.exp}")
    private long expTime;

    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    @Autowired
    private RedisTemplate<String, String> redis;

    public String generateToken(SessionUserDto user) {
        return tokenPrefix + JWT.create()
                .withSubject(user.getId().toString())
                .withClaim("name", user.getName())
                .withClaim("teacher", user.getIsTeacher())
                .withExpiresAt(new Date(System.currentTimeMillis() + expTime))
                .sign(Algorithm.HMAC512(secret));
    }

    private void checkBlackList(String token) throws Exception {
        // 检查黑名单
        Integer id = getLoginUserId(token);
        Set<String> tokens = redis.opsForSet().members("logout" + id);
        if (tokens != null) {
            for (String t : tokens) {
                if (t.equals(token)) {
                    throw new Exception();
                }
            }
        }
    }

    public boolean validateToken(String token) {

        try {
            checkBlackList(token);
            JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyIdentity(String token) {
        try {
            checkBlackList(token);
            boolean teacher = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getClaim("teacher").asBoolean();
            return teacher;
        } catch (Exception e) {
            return false;
        }
    }

    public Date getExpireDate(String token) throws Exception {
        try {

            Date date = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getExpiresAt();
            return date;
        } catch (Exception e) {
            throw new Exception("身份验证失败");
        }
    }

    public Integer getLoginUserId(String token) {
        try {
            String id = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getSubject();
            return Integer.valueOf(id);
        } catch (Exception e) {
            return null;
        }
    }

    public String getLoginUserName(String token) {
        try {
            String name = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getClaim("name").asString();
            return name;
        } catch (Exception e) {
            return null;
        }
    }
}