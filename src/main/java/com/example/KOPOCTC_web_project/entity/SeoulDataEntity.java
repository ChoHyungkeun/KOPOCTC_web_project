package com.example.KOPOCTC_web_project.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeoulDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_id", unique = true)
    private String serviceId;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "area_name")
    private String areaName;

    @Column(name = "place_name")
    private String placeName;

    @Column(name = "category")
    private String category;  // 체육시설, 문화시설, 교육, 진료, 기타

    @Column(name = "use_target")
    private String useTarget;

    @Column(name = "service_url", length = 500)
    private String serviceUrl;

    @Column(name = "reservation_method")
    private String reservationMethod;

    @Column(name = "pay_at_place")
    private String payAtPlace;

    @Column(name = "service_status")
    private String serviceStatus;

    @Column(name = "tel_no")
    private String telNo;

    @Column(name = "address")
    private String address;

    @Column(name = "min_class_time")
    private String minClassTime;

    @Column(name = "max_class_time")
    private String maxClassTime;

    @Column(name = "reservation_start_date")
    private String reservationStartDate;

    @Column(name = "reservation_end_date")
    private String reservationEndDate;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "detail_content", length = 1000)
    private String detailContent;

    @Column(name = "x")
    private Double xCoordinate;

    @Column(name = "y")
    private Double yCoordinate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private int recommendCount = 0;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}