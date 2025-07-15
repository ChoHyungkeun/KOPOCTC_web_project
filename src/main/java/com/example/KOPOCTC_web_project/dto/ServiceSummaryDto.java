package com.example.KOPOCTC_web_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("image_url")
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
        this.imageUrl = (entity.getImageUrl() == null || entity.getImageUrl().isEmpty()) ? "https://data.seoul.go.kr/resources/img/common/logo.png" : entity.getImageUrl();
    }

    public String getBadgeClass() {
        if ("접수중".equals(serviceStatus)) {
            return "primary";
        } else if ("예약마감".equals(serviceStatus)) {
            return "danger";
        } else if ("안내중".equals(serviceStatus)) {
            return "info";
        } else if ("접수종료".equals(serviceStatus)) {
            return "secondary";
        } else {
            return "dark";  // 기본 색상
        }
    }

    public String getCategoryClass() {
        if ("교육".equals(category)) {
            return "success";
        } else if ("체육시설".equals(category)) {
            return "primary";
        } else if ("문화시설".equals(category)) {
            return "info";
        } else if ("시설대관".equals(category)) {
            return "warning";
        } else if ("진료".equals(category)) {
            return "danger";
        } else {
            return "dark";  // 기본 색상
        }
    }
}

