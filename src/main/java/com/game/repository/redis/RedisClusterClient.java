package com.game.repository.redis;

import com.game.config.RedisClusterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedisClusterClient {


    @Autowired
    RedisClusterConfig redisClusterConfig;

    //@Autowired
    RedisConnectionFactory connectionFactory;

    //@PostConstruct
    public RedisConnectionFactory connectionFactory() {
        JedisConnectionFactory jedisConnectionFactory =  new JedisConnectionFactory(
                new RedisClusterConfiguration(redisClusterConfig.getNodes()));
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate();
        connectionFactory = connectionFactory();
        template.setConnectionFactory(connectionFactory);
        return template;
    }


}
