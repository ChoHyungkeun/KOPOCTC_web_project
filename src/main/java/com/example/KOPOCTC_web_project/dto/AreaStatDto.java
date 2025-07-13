package com.example.KOPOCTC_web_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 지역별 통계 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaStatDto {
    private String area;
    private Long count;
}
