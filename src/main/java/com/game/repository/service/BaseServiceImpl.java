package com.game.repository.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;


public class BaseServiceImpl {

    @Autowired
    protected RedisTemplate<String, Object> redisTemplate;


    public static final long DEFAULT_TIMEOUT = 60;

    public static final String TABLE_USER = "cs_user";

    public static final String DELIMITER = ":" ;
}
