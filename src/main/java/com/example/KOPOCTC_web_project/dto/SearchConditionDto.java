package com.example.KOPOCTC_web_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 검색 조건 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchConditionDto {
    private String category;        // 카테고리
    private String area;           // 지역
    private String keyword;        // 검색어
    private String status;         // 서비스 상태
    private int page = 0;          // 페이지 번호 (0부터 시작)
    private int size = 20;         // 페이지 크기
    private String sortBy = "serviceName";  // 정렬 기준
    private String sortDir = "ASC";         // 정렬 방향
}
