package com.example.KOPOCTC_web_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_recommends", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "service_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRecommend {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false)
    private SeoulDataEntity service;

    @Column(name = "recommended_at", nullable = false)
    private LocalDateTime recommendedAt;

    @PrePersist
    public void onCreate() {
        this.recommendedAt = LocalDateTime.now();
    }
}
