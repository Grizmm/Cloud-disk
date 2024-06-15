package com.easypan.entity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
public class RedisConfig<V> {
    @Bean
    public RedisTemplate<String, V> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, V> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        //设置key 的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        //设置value 的序列化方式
        template.setValueSerializer(RedisSerializer.json());
        //设置hash的key 的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        //设置hash的value 的序列化方式
        template.setHashValueSerializer(RedisSerializer.json());
        return template;
    }
}