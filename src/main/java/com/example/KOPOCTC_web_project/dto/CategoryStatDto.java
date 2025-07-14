package com.example.KOPOCTC_web_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 카테고리별 통계 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryStatDto {
    private String category;
    private Long count;
}
