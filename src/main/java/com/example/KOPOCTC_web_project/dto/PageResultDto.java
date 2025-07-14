package com.example.KOPOCTC_web_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 페이징 결과 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResultDto<T> {
    private List<T> content;           // 실제 데이터
    private int totalPages;            // 전체 페이지 수
    private long totalElements;        // 전체 요소 수
    private int currentPage;           // 현재 페이지
    private int pageSize;              // 페이지 크기
    private boolean hasNext;           // 다음 페이지 존재 여부
    private boolean hasPrevious;       // 이전 페이지 존재 여부
    private boolean isFirst;           // 첫 번째 페이지 여부
    private boolean isLast;            // 마지막 페이지 여부

    // Spring Data의 Page 객체를 PageResultDto로 변환하는 생성자
    public PageResultDto(org.springframework.data.domain.Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
        this.isFirst = page.isFirst();
        this.isLast = page.isLast();
    }
}
