package com.maidf.javaquiz.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.maidf.javaquiz.entity.req.StartAnsReq;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigurationProperties("ans")
@Component
@Data
public class AnsTokenUtil {
    private String secret;
    private Long exp;

    @Autowired
    private RedisTemplate<String, String> redis;

    public String generateToken(StartAnsReq ansMsg, Long userId) {
        Long now = System.currentTimeMillis();
        log.info("生成token");
        return JWT.create()
                .withSubject(userId.toString())
                .withClaim("qn", ansMsg.getQuestionId())
                .withClaim("paper", ansMsg.getPaperId())
                .withClaim("time", now)
                .withExpiresAt(new Date(System.currentTimeMillis() + exp + 30000))
                .sign(Algorithm.HMAC512(secret));
    }

    public String generateToken(StartAnsReq ansMsg, Long userId, Long exp) {
        Long now = System.currentTimeMillis();
        log.info("生成token");
        return JWT.create()
                .withSubject(userId.toString())
                .withClaim("qn", ansMsg.getQuestionId())
                .withClaim("paper", ansMsg.getPaperId())
                .withClaim("time", now)
                .withExpiresAt(new Date(System.currentTimeMillis() + exp + 30000))
                .sign(Algorithm.HMAC512(secret));
    }

    public void validateToken(String token) throws Exception {
        try {
            log.info("验证token:{}", token);
            checkBlackList(token);
            JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token);
        } catch (Exception e) {
            throw new Exception("无效token");
        }
    }

    private void checkBlackList(String token) throws Exception {
        // 检查黑名单
        log.info("检查黑名单");
        Long userId = getUserId(token);
        Set<String> tokens = redis.opsForSet().members("black_ans_token" + userId);
        if (tokens != null) {
            for (String t : tokens) {
                if (t.equals(token)) {
                    throw new Exception();
                }
            }
        }
    }

    public void blackToken(String token) throws Exception {
        try {
            Long userId = getUserId(token);
            redis.opsForSet().add("black_ans_token" + userId, token);
            redis.expire("black_ans_token" + userId, getExp() / 1000 / 60, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new Exception("拉黑token失败");
        }
    }

    public Long getUserId(String token) throws Exception {
        try {
            String id = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token)
                    .getSubject();
            log.info("获取user_id: {}", id);
            return Long.valueOf(id);
        } catch (Exception e) {
            throw new Exception("token中没有user_id");
        }
    }

    public Long getQnId(String token) throws Exception {
        try {
            Long id = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token)
                    .getClaim("qn").asLong();
            log.info("获取qn_id: {}", id);
            return id;
        } catch (Exception e) {
            throw new Exception("token中没有qn_id");
        }
    }

    public Long getPaperId(String token) throws Exception {
        try {
            Long id = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token)
                    .getClaim("paper").asLong();
            log.info("获取paper_id: {}", id);
            return id;
        } catch (Exception e) {
            throw new Exception("token中没有paper_id");
        }
    }

    public Timestamp getStartTime(String token) throws Exception {
        try {
            Long time = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token)
                    .getClaim("time").asLong();
            log.info("获取start_time: {}", time);
            return new Timestamp(time);
        } catch (Exception e) {
            throw new Exception("token中没有start_time");
        }
    }
}
