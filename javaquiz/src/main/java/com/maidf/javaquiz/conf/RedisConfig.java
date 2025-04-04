package com.maidf.javaquiz.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Long> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // 设置键的序列化器为 StringRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 设置值的序列化器为 GenericToStringSerializer
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));

        // 设置哈希键和值的序列化器（可选）
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Long.class));

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}