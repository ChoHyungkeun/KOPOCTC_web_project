package com.example.KOPOCTC_web_project.repository;

import com.example.KOPOCTC_web_project.entity.RefreshToken;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
public class RefreshTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;

    public RefreshTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String username, String refreshToken, Duration ttl) {
        redisTemplate.opsForValue().set("refresh_" + username, refreshToken, ttl);
    }

    public void deleteById(String username) {
        redisTemplate.delete("refresh_" + username);
    }

    public Optional<String> findById(String username) {
        return Optional.ofNullable(redisTemplate.opsForValue().get("refresh_" + username));
    }
}
