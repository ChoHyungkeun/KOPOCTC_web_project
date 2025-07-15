package com.example.KOPOCTC_web_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 서비스 상세 정보 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetailDto {
    private Long id;
    private String serviceId;
    private String serviceName;
    private String areaName;
    private String placeName;
    private String category;
    private String useTarget;
    private String serviceUrl;
    private String reservationMethod;
    private String payAtPlace;
    private String serviceStatus;
    private String telNo;
    private String address;
    private String minClassTime;
    private String maxClassTime;
    private String reservationStartDate;
    private String reservationEndDate;
    private String imageUrl;
    private String detailContent;
    @JsonProperty("x")
    private Double xCoordinate;
    @JsonProperty("y")
    private Double yCoordinate;
    private int recommendCount;

    // Entity에서 DTO로 변환하는 생성자
    public ServiceDetailDto(com.example.KOPOCTC_web_project.entity.SeoulDataEntity entity) {
        this.id = entity.getId();
        this.serviceId = entity.getServiceId();
        this.serviceName = entity.getServiceName();
        this.areaName = entity.getAreaName();
        this.placeName = entity.getPlaceName();
        this.category = entity.getCategory();
        this.useTarget = entity.getUseTarget();
        this.serviceUrl = entity.getServiceUrl();
        this.reservationMethod = entity.getReservationMethod();
        this.payAtPlace = entity.getPayAtPlace();
        this.serviceStatus = entity.getServiceStatus();
        this.telNo = entity.getTelNo();
        this.address = entity.getAddress();
        this.minClassTime = entity.getMinClassTime();
        this.maxClassTime = entity.getMaxClassTime();
        this.reservationStartDate = entity.getReservationStartDate();
        this.reservationEndDate = entity.getReservationEndDate();
        this.imageUrl = entity.getImageUrl();
        this.detailContent = entity.getDetailContent();
        this.xCoordinate = entity.getXCoordinate();
        this.yCoordinate = entity.getYCoordinate();
        this.recommendCount = entity.getRecommendCount();
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
}
