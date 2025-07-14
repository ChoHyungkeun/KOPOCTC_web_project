package com.example.KOPOCTC_web_project.util;

import com.example.KOPOCTC_web_project.dto.ApiResponseDto;
import com.example.KOPOCTC_web_project.dto.ServiceReservationDto;
import com.example.KOPOCTC_web_project.entity.SeoulDataEntity;
import com.example.KOPOCTC_web_project.repository.SeoulPublicServiceRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataImporter {

    private final SeoulPublicServiceRepository repository;
    private final RestTemplate restTemplate;

    @Value("${seoul.api.key}")
    private String apiKey;

    private static final String BASE_URL = "http://openapi.seoul.go.kr:8088";

    @EventListener(ApplicationReadyEvent.class)
    public void importInitialData() {
        if (repository.count() == 0) {
            log.info("초기 데이터 로딩 시작...");
            importAllServiceData();
        }
    }

    public void importAllServiceData() {
        try {
            // 여러 서비스 타입별로 데이터 가져오기
            importServiceByType("ListPublicReservationSport", "체육시설");
            importServiceByType("ListPublicReservationCulture", "문화시설");
            importServiceByType("ListPublicReservationEducation", "교육");
            importServiceByType("ListPublicReservationMedical", "진료");
            importServiceByType("ListPublicReservationInstitution", "시설대관");

            log.info("총 {}개의 서비스 데이터가 저장되었습니다.", repository.count());

        } catch (Exception e) {
            log.error("데이터 임포트 중 오류 발생", e);
        }
    }

    private void importServiceByType(String serviceType, String category) {
        int start = 1;
        int batchSize = 1000;
        boolean hasMore = true;

        while (hasMore) {
            try {
                String url = String.format("%s/%s/json/%s/%d/%d",
                        BASE_URL, apiKey, serviceType, start, start + batchSize - 1);

                log.info("API 호출: {}", url);

                ResponseEntity<ApiResponseDto<ServiceReservationDto>> response =
                        restTemplate.exchange(
                                url,
                                HttpMethod.GET,
                                null,
                                new ParameterizedTypeReference<ApiResponseDto<ServiceReservationDto>>() {}
                        );

                if (response.getBody() != null) {
                    List<ServiceReservationDto> dtoList = response.getBody().getRowsByServiceType(serviceType);

                    if (dtoList == null || dtoList.isEmpty()) {
                        hasMore = false;
                        continue;
                    }

                    log.info("파싱된 데이터 개수: {}", dtoList != null ? dtoList.size() : "null");
                    if (dtoList != null && !dtoList.isEmpty()) {
                        log.info("첫 번째 데이터: {}", dtoList.get(0));
                    }

                    List<SeoulDataEntity> entities = dtoList.stream()
                            .map(dto -> convertToEntity(dto, category))
                            .collect(Collectors.toList());

                    repository.saveAll(entities);
                    log.info("{} 카테고리 {}개 데이터 저장 완료", category, entities.size());

                    start += batchSize;

                    // API 호출 제한을 위한 딜레이
                    Thread.sleep(100);

                } else {
                    hasMore = false;
                }

            } catch (Exception e) {
                log.error("API 호출 실패 - 카테고리: {}, 시작: {}", category, start, e);
                hasMore = false;
            }
        }
    }

    private SeoulDataEntity convertToEntity(ServiceReservationDto dto, String category) {
        SeoulDataEntity entity = new SeoulDataEntity();
        entity.setServiceId(dto.getSvcid());
        entity.setServiceName(dto.getSvcnm());
        entity.setAreaName(dto.getAreanm());
        entity.setPlaceName(dto.getPlacenm());
        entity.setUseTarget(dto.getUsetgtinfo());
        entity.setServiceUrl(dto.getSvcurl());
        entity.setReservationMethod(dto.getRcptbgndt());
        entity.setPayAtPlace(dto.getPayatnm());
        entity.setServiceStatus(dto.getSvcstatnm());
        entity.setCategory(category);
        entity.setTelNo(dto.getTelno());
        entity.setAddress(dto.getAdres());
        entity.setMinClassTime(dto.getMinclassnm());
        entity.setMaxClassTime(dto.getMaxclassnm());
        entity.setReservationStartDate(dto.getRcptbgndt());
        entity.setReservationEndDate(dto.getRcptenddt());
        entity.setXCoordinate(dto.getX());
        entity.setYCoordinate(dto.getY());

        return entity;
    }

    // 수동으로 데이터 업데이트할 때 사용
    public void refreshData() {
        log.info("기존 데이터 삭제 및 새로운 데이터 로딩 시작...");
        repository.deleteAll();
        importAllServiceData();
    }
}
