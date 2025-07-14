package com.example.KOPOCTC_web_project.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class AccessTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public AccessTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveBlackList(String accessToken, Duration ttl) {
        redisTemplate.opsForValue().set("BL_" + accessToken, "logout", ttl);
        System.out.println("블랙리스트 저장: token=" + accessToken + ", ttl=" + ttl);
    }

    public boolean isBlackListed(String accessToken) {
        return redisTemplate.hasKey("BL_" + accessToken);
    }
}
