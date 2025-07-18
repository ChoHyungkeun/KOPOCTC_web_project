package com.example.KOPOCTC_web_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 서비스 예약 정보 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceReservationDto {
    @JsonProperty("SVCID")
    private String svcid;           // 서비스ID

    @JsonProperty("MAXCLASSNM")
    private String maxclassnm;      // 최대 수강 정원

    @JsonProperty("MINCLASSNM")
    private String minclassnm;      // 최소 수강 정원

    @JsonProperty("SVCSTATNM")
    private String svcstatnm;       // 서비스 상태

    @JsonProperty("SVCNM")
    private String svcnm;           // 서비스명

    @JsonProperty("PAYATNM")
    private String payatnm;         // 결제 방법

    @JsonProperty("PLACENM")
    private String placenm;         // 장소명

    @JsonProperty("USETGTINFO")
    private String usetgtinfo;      // 서비스 대상

    @JsonProperty("SVCURL")
    private String svcurl;          // 서비스 URL

    @JsonProperty("X")
    private Double x;               // X좌표

    @JsonProperty("Y")
    private Double y;               // Y좌표

    @JsonProperty("SVCOPNBGNDT")
    private String svcopnbgndt;     // 서비스 개방 시작일

    @JsonProperty("SVCOPNENDDT")
    private String svcopnenddt;     // 서비스 개방 종료일

    @JsonProperty("RCPTBGNDT")
    private String rcptbgndt;       // 접수 시작일

    @JsonProperty("RCPTENDDT")
    private String rcptenddt;       // 접수 종료일

    @JsonProperty("AREANM")
    private String areanm;          // 지역명

    @JsonProperty("IMGURL")
    private String imgurl;          // 이미지 URL

    @JsonProperty("DTLCONT")
    private String dtlcont;         // 상세 내용

    @JsonProperty("TELNO")
    private String telno;           // 전화번호

    @JsonProperty("V")
    private String v;               // V(미사용)

    @JsonProperty("REVSTDDAY")
    private String revstdday;       // 취소 기준일

    @JsonProperty("REVSTDDAYNM")
    private String revstddaynm;     // 취소 기준일명

    @JsonProperty("ADRES")
    private String adres;           // 주소

    // 날짜 포맷: 2025-07-31
    public String getRcptbgndt() {
        return formatDate(rcptbgndt);
    }

    public String getRcptenddt() {
        return formatDate(rcptenddt);
    }

    private String formatDate(String input) {
        if (input == null || input.isBlank()) return "";
        try {
            return input.substring(0, 19); // "2025-07-31 00:00:00.0" → "2025-07-31"
        } catch (Exception e) {
            return input; // 실패 시 원본 반환
        }
    }
}
