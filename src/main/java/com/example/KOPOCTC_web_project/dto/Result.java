package com.example.KOPOCTC_web_project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// API 결과 상태 정보
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;
    private String message;
}
