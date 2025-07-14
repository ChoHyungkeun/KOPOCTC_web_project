package com.example.KOPOCTC_web_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// API 응답 최상위 DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<T> {

    @JsonProperty("ListPublicReservationSport")
    private ListPublicReservationSport<T> listPublicReservationSport;
    @JsonProperty("ListPublicReservationCulture")
    private ListPublicReservationCulture<T> listPublicReservationCulture;
    @JsonProperty("ListPublicReservationEducation")
    private ListPublicReservationEducation<T> listPublicReservationEducation;
    @JsonProperty("ListPublicReservationMedical")
    private ListPublicReservationMedical<T> listPublicReservationMedical;
    @JsonProperty("ListPublicReservationInstitution")
    private ListPublicReservationInstitution<T> listPublicReservationInstitution;

    // 공통 메서드로 데이터 추출
    public List<T> getDataList() {
        if (listPublicReservationSport != null) {
            return listPublicReservationSport.getRow();
        }
        if (listPublicReservationCulture != null) {
            return listPublicReservationCulture.getRow();
        }
        if (listPublicReservationEducation != null) {
            return listPublicReservationEducation.getRow();
        }
        if (listPublicReservationMedical != null) {
            return listPublicReservationMedical.getRow();
        }
        if (listPublicReservationInstitution != null) {
            return listPublicReservationInstitution.getRow();
        }
        return null;
    }

    public List<T> getRowsByServiceType(String type) {
        return switch (type) {
            case "ListPublicReservationSport" ->
                    listPublicReservationSport != null ? listPublicReservationSport.getRow() : List.of();
            case "ListPublicReservationCulture" ->
                    listPublicReservationCulture != null ? listPublicReservationCulture.getRow() : List.of();
            case "ListPublicReservationEducation" ->
                    listPublicReservationEducation != null ? listPublicReservationEducation.getRow() : List.of();
            case "ListPublicReservationMedical" ->
                    listPublicReservationMedical != null ? listPublicReservationMedical.getRow() : List.of();
            case "ListPublicReservationInstitution" ->
                    listPublicReservationInstitution != null ? listPublicReservationInstitution.getRow() : List.of();
            default -> List.of();
        };
    }
}
