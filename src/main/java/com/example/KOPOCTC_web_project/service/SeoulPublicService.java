package com.example.KOPOCTC_web_project.service;

import com.example.KOPOCTC_web_project.dto.*;
import com.example.KOPOCTC_web_project.entity.SeoulDataEntity;
import com.example.KOPOCTC_web_project.repository.SeoulPublicServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SeoulPublicService {

    private final SeoulPublicServiceRepository repository;

    // 페이징 및 필터링을 적용한 서비스 목록 조회
    public PageResultDto<ServiceSummaryDto> getServiceList(SearchConditionDto searchCondition) {

        // 정렬 조건 생성
        Sort sort = createSort(searchCondition.getSortBy(), searchCondition.getSortDir());

        // 페이지 요청 생성
        Pageable pageable = PageRequest.of(searchCondition.getPage(), searchCondition.getSize(), sort);

        // 검색 조건에 따른 데이터 조회
        Page<SeoulDataEntity> page = repository.findBySearchConditions(
                searchCondition.getCategory(),
                searchCondition.getArea(),
                searchCondition.getKeyword(),
                searchCondition.getStatus(),
                pageable
        );

        // Entity를 DTO로 변환
        Page<ServiceSummaryDto> dtoPage = page.map(ServiceSummaryDto::new);

        return new PageResultDto<>(dtoPage);
    }

    // 서비스 상세 정보 조회
    public ServiceDetailDto getServiceDetail(Long id) {
        Optional<SeoulDataEntity> service = repository.findById(id);
        return service.map(ServiceDetailDto::new).orElse(null);
    }

    // 카테고리별 서비스 목록 조회
    public PageResultDto<ServiceSummaryDto> getServicesByCategory(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("serviceName").ascending());
        Page<SeoulDataEntity> servicePage = repository.findByCategory(category, pageable);
        Page<ServiceSummaryDto> dtoPage = servicePage.map(ServiceSummaryDto::new);

        return new PageResultDto<>(dtoPage);
    }

    // 지역별 서비스 목록 조회
    public PageResultDto<ServiceSummaryDto> getServicesByArea(String area, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("serviceName").ascending());
        Page<SeoulDataEntity> servicePage = repository.findByAreaNameContaining(area, pageable);
        Page<ServiceSummaryDto> dtoPage = servicePage.map(ServiceSummaryDto::new);

        return new PageResultDto<>(dtoPage);
    }

    // 인기 서비스 조회 (예약 가능한 서비스 우선)
    public PageResultDto<ServiceSummaryDto> getPopularServices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SeoulDataEntity> servicePage = repository.findPopularServices(pageable);
        Page<ServiceSummaryDto> dtoPage = servicePage.map(ServiceSummaryDto::new);

        return new PageResultDto<>(dtoPage);
    }

    // 최근 등록된 서비스 조회
    public PageResultDto<ServiceSummaryDto> getRecentServices(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SeoulDataEntity> servicePage = repository.findAllByOrderByCreatedAtDesc(pageable);
        Page<ServiceSummaryDto> dtoPage = servicePage.map(ServiceSummaryDto::new);

        return new PageResultDto<>(dtoPage);
    }

    /*
    // 통계 정보 조회
    public List<CategoryStatDto> getCategoryStatistics() {
        return repository.getCategoryStatistics();
    }

    public List<AreaStatDto> getAreaStatistics() {
        return repository.getAreaStatistics();
    }
     */

    // 필터 옵션 조회
    public List<String> getCategories() {
        return repository.findDistinctCategories();
    }

    public List<String> getAreas() {
        return repository.findDistinctAreaNames();
    }

    public List<String> getServiceStatuses() {
        return repository.findDistinctServiceStatuses();
    }

    public List<String> getAreasByCategory(String category) {
        return repository.findDistinctAreaNamesByCategory(category);
    }

    // 전체 서비스 수 조회
    public long getTotalServiceCount() {
        return repository.count();
    }

    // 카테고리별 서비스 수 조회
    public long getServiceCountByCategory(String category) {
        return repository.findByCategory(category, Pageable.unpaged()).getTotalElements();
    }

    // 검색 키워드로 서비스 조회
    public PageResultDto<ServiceSummaryDto> searchServices(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("serviceName").ascending());
        Page<SeoulDataEntity> servicePage = repository.findByServiceNameContaining(keyword, pageable);
        Page<ServiceSummaryDto> dtoPage = servicePage.map(ServiceSummaryDto::new);

        return new PageResultDto<>(dtoPage);
    }

    // 정렬 조건 생성 헬퍼 메서드
    private Sort createSort(String sortBy, String sortDir) {
        Sort.Direction direction = "DESC".equalsIgnoreCase(sortDir) ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        switch (sortBy) {
            case "serviceName":
                return Sort.by(direction, "serviceName");
            case "areaName":
                return Sort.by(direction, "areaName");
            case "category":
                return Sort.by(direction, "category");
            case "createdAt":
                return Sort.by(direction, "createdAt");
            case "serviceStatus":
                return Sort.by(direction, "serviceStatus");
            default:
                return Sort.by(Sort.Direction.ASC, "serviceName");
        }
    }
}