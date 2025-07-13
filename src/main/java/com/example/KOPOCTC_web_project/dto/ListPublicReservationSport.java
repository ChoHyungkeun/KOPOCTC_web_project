package com.example.KOPOCTC_web_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 각 서비스별 래퍼 클래스들
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListPublicReservationSport<T> {
    @JsonProperty("list_total_count")
    private Integer listTotalCount;
    @JsonProperty("RESULT")
    private Result result;
    @JsonProperty("row")
    private List<T> row;
}
