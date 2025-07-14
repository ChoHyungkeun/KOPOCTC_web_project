package com.example.KOPOCTC_web_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 서비스 요약 정보 DTO (목록용)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceSummaryDto {
    private Long id;
    private String serviceName;
    private String areaName;
    private String placeName;
    private String category;
    private String serviceStatus;
    private String reservationStartDate;
    private String reservationEndDate;
    private String imageUrl;

    // Entity에서 DTO로 변환하는 생성자
    public ServiceSummaryDto(com.example.KOPOCTC_web_project.entity.SeoulDataEntity entity) {
        this.id = entity.getId();
        this.serviceName = entity.getServiceName();
        this.areaName = entity.getAreaName();
        this.placeName = entity.getPlaceName();
        this.category = entity.getCategory();
        this.serviceStatus = entity.getServiceStatus();
        this.reservationStartDate = entity.getReservationStartDate();
        this.reservationEndDate = entity.getReservationEndDate();
        this.imageUrl = entity.getImageUrl();
    }
}

