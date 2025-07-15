package com.example.KOPOCTC_web_project.repository;

import com.example.KOPOCTC_web_project.dto.AreaStatDto;
import com.example.KOPOCTC_web_project.dto.CategoryStatDto;
import com.example.KOPOCTC_web_project.entity.SeoulDataEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeoulPublicServiceRepository extends JpaRepository<SeoulDataEntity, Long> {

    // 카테고리별 페이징 조회
    Page<SeoulDataEntity> findByCategory(String category, Pageable pageable);

    // 지역별 페이징 조회
    Page<SeoulDataEntity> findByAreaNameContaining(String areaName, Pageable pageable);

    // 서비스명으로 검색 (페이징)
    Page<SeoulDataEntity> findByServiceNameContaining(String serviceName, Pageable pageable);

    // 서비스 상태별 조회 (페이징)
    Page<SeoulDataEntity> findByServiceStatus(String status, Pageable pageable);

    // 복합 검색 (카테고리 + 지역 + 키워드)
    @Query("SELECT s FROM SeoulDataEntity s WHERE " +
            "(:category IS NULL OR s.category = :category) AND " +
            "(:area IS NULL OR s.areaName LIKE :area) AND " +
            "(:keyword IS NULL OR s.serviceName LIKE :keyword OR s.placeName LIKE :keyword) AND " +
            "(:status IS NULL OR s.serviceStatus = :status)")
    Page<SeoulDataEntity> findBySearchConditions(
            @Param("category") String category,
            @Param("area") String area,
            @Param("keyword") String keyword,
            @Param("status") String status,
            Pageable pageable);
/*
    // 카테고리별 통계
    @Query("SELECT new com.example.seoul.dto.CategoryStatDto(s.category, COUNT(s)) " +
            "FROM SeoulDataEntity s " +
            "GROUP BY s.category " +
            "ORDER BY COUNT(s) DESC")
    List<CategoryStatDto> getCategoryStatistics();

    // 지역별 통계
    @Query("SELECT new com.example.seoul.dto.AreaStatDto(s.areaName, COUNT(s)) " +
            "FROM SeoulDataEntity s " +
            "GROUP BY s.areaName " +
            "ORDER BY COUNT(s) DESC")
    List<AreaStatDto> getAreaStatistics();
*/
    // 고유 카테고리 목록 조회
    @Query("SELECT DISTINCT s.category FROM SeoulDataEntity s ORDER BY s.category")
    List<String> findDistinctCategories();

    // 고유 지역 목록 조회
    @Query("SELECT DISTINCT s.areaName FROM SeoulDataEntity s ORDER BY s.areaName")
    List<String> findDistinctAreaNames();

    // 고유 서비스 상태 목록 조회
    @Query("SELECT DISTINCT s.serviceStatus FROM SeoulDataEntity s ORDER BY s.serviceStatus")
    List<String> findDistinctServiceStatuses();

    // 카테고리별 지역 목록 조회
    @Query("SELECT DISTINCT s.areaName FROM SeoulDataEntity s " +
            "WHERE s.category = :category ORDER BY s.areaName")
    List<String> findDistinctAreaNamesByCategory(@Param("category") String category);

    // 인기 서비스 조회 (예약 가능한 서비스 우선)
    @Query("SELECT s FROM SeoulDataEntity s " +
            "WHERE s.serviceStatus IN ('접수중', '안내중') " +
            "ORDER BY s.serviceName")
    Page<SeoulDataEntity> findPopularServices(Pageable pageable);

    // 최근 등록된 서비스 조회
    Page<SeoulDataEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 특정 지역의 특정 카테고리 서비스 조회
    Page<SeoulDataEntity> findByCategoryAndAreaNameContaining(
            String category, String areaName, Pageable pageable);
}
