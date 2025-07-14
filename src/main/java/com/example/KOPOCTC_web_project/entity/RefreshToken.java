package com.example.KOPOCTC_web_project.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 2) // 2시간 TTL
public class RefreshToken {
    @Id
    private String username;
    private String refreshToken;
}
