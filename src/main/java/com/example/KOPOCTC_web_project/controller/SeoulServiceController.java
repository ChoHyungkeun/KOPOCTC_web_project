package com.example.KOPOCTC_web_project.controller;

import com.example.KOPOCTC_web_project.dto.*;
import com.example.KOPOCTC_web_project.entity.Article;
import com.example.KOPOCTC_web_project.repository.ArticleRepository;
import com.example.KOPOCTC_web_project.security.CustomUserDetails;
import com.example.KOPOCTC_web_project.service.SeoulPublicService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SeoulServiceController {
    @Autowired
    private ArticleRepository articleRepository;
    private final SeoulPublicService service;

    // 메인 페이지
    @GetMapping("/svclist")
    public String index(Model model) {
        // 통계 정보 조회
        //List<CategoryStatDto> categoryStats = service.getCategoryStatistics();
        //List<AreaStatDto> areaStats = service.getAreaStatistics();

        // 필터 옵션 조회
        List<String> categories = service.getCategories();
        List<String> areas = service.getAreas();
        List<String> statuses = service.getServiceStatuses();

        // 인기 서비스 조회 (상위 6개)
        PageResultDto<ServiceSummaryDto> popularServices = service.getPopularServices(0, 6);

        // 최근 등록 서비스 조회 (상위 6개)
        PageResultDto<ServiceSummaryDto> recentServices = service.getRecentServices(0, 6);

        //model.addAttribute("categoryStats", categoryStats);
        //model.addAttribute("areaStats", areaStats);
        model.addAttribute("categories", categories);
        model.addAttribute("areas", areas);
        model.addAttribute("statuses", statuses);
        model.addAttribute("popularServices", popularServices.getContent());
        model.addAttribute("recentServices", recentServices.getContent());
        model.addAttribute("totalCount", service.getTotalServiceCount());

        return "articles/serviceList";
    }

    // 서비스 목록 페이지 (페이징 및 필터링)
    @GetMapping("/services")
    public String listServices(
            @RequestParam(defaultValue = "") String category,
            @RequestParam(defaultValue = "") String area,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "serviceName") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir,
            Model model) {

        // 검색 조건 생성
        System.out.println("category=" + category + ", area=" + area + ", keyword=" + keyword + ", status=" + status);

        SearchConditionDto searchCondition = new SearchConditionDto();
        searchCondition.setCategory(category.isEmpty() ? null : category);
        searchCondition.setArea(area.isEmpty() ? null : area);
        searchCondition.setKeyword(keyword.isEmpty() ? null : keyword);
        searchCondition.setStatus(status.isEmpty() ? null : status);
        searchCondition.setPage(page);
        searchCondition.setSize(size);
        searchCondition.setSortBy(sortBy);
        searchCondition.setSortDir(sortDir);
        Map<String, Object> searchConditionMap = new HashMap<>();
        searchConditionMap.put("category", Optional.ofNullable(searchCondition.getCategory()).orElse(""));
        searchConditionMap.put("area", Optional.ofNullable(searchCondition.getArea()).orElse(""));
        searchConditionMap.put("keyword", Optional.ofNullable(searchCondition.getKeyword()).orElse(""));
        searchConditionMap.put("status", Optional.ofNullable(searchCondition.getStatus()).orElse(""));
        searchConditionMap.put("page", searchCondition.getPage());
        searchConditionMap.put("size", searchCondition.getSize());
        searchConditionMap.put("sortBy", searchCondition.getSortBy());
        searchConditionMap.put("sortDir", searchCondition.getSortDir());

        model.addAttribute("searchCondition", searchConditionMap);

        // 서비스 목록 조회
        PageResultDto<ServiceSummaryDto> serviceList = service.getServiceList(searchCondition);
        serviceList.getContent().forEach(dto -> {
            if (dto.getServiceName() != null) {
                dto.setServiceName(HtmlUtils.htmlUnescape(dto.getServiceName()));
            }
        });

        // 필터 옵션 리스트
        List<String> categories = service.getCategories();
        List<String> areas = service.getAreas();
        List<String> statuses = service.getServiceStatuses();

        // 옵션 DTO 리스트 생성 (선택값 포함)
        List<SelectOptionDto> categoryOptions = toSelectOptions(categories, searchCondition.getCategory());
        List<SelectOptionDto> areaOptions = toSelectOptions(areas, searchCondition.getArea());
        List<SelectOptionDto> statusOptions = toSelectOptions(statuses, searchCondition.getStatus());

        // 페이징 번호 리스트 (현재 페이지 ± 2)
        int totalPages = serviceList.getTotalPages();  // 전체 페이지 수 (0-based)
        int maxButtons = 5;  // 최대 보여줄 페이지 버튼 수

        // 현재 페이지 (0-based)
        int currentPage = page;

        // 시작 페이지 번호 계산
        int half = maxButtons / 2;
        int startPage = currentPage - half;
        int endPage = currentPage + half;

        // 시작 페이지가 0보다 작으면 보정
        if (startPage < 0) {
            startPage = 0;
            endPage = Math.min(maxButtons - 1, totalPages - 1);
        }

        // 끝 페이지가 전체 페이지 수보다 크면 보정
        if (endPage > totalPages - 1) {
            endPage = totalPages - 1;
            startPage = Math.max(0, endPage - maxButtons + 1);
        }

        // 페이지 버튼 리스트 생성
        List<PageNumberDto> pageNumbers = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            pageNumbers.add(new PageNumberDto(i, i == currentPage));
        }

        // 이전/다음 페이지 번호 계산
        int prevPage = (page > 0) ? page - 1 : 0;
        int nextPage = (page < totalPages - 1) ? page + 1 : totalPages - 1;

        System.out.println("searchCondition == null? " + (searchCondition == null));
        System.out.println("searchCondition class = " + searchCondition.getClass().getName());
        System.out.println("searchCondition keyword = " + searchCondition.getKeyword());
        // 정렬 및 페이지 크기 관련 편의 변수
        model.addAttribute("sortByServiceName", "serviceName".equalsIgnoreCase(sortBy));
        model.addAttribute("sortByCategory", "category".equalsIgnoreCase(sortBy));
        model.addAttribute("sortDirAsc", "ASC".equalsIgnoreCase(sortDir));
        model.addAttribute("sortDirDesc", "DESC".equalsIgnoreCase(sortDir));
        model.addAttribute("size10", size == 10);
        model.addAttribute("size20", size == 20);
        model.addAttribute("size50", size == 50);

        // 모델에 담기
        model.addAttribute("serviceList", serviceList);
        model.addAttribute("categoryOptions", categoryOptions);
        model.addAttribute("areaOptions", areaOptions);
        model.addAttribute("statusOptions", statusOptions);
        //model.addAttribute("searchCondition", searchCondition);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);

        return "articles/searchResult";
    }

    private List<SelectOptionDto> toSelectOptions(List<String> items, String selectedValue) {
        List<SelectOptionDto> options = new ArrayList<>();
        for (String item : items) {
            boolean selected = item.equals(selectedValue);
            options.add(new SelectOptionDto(item, item, selected));
        }
        return options;
    }

    @Value("${kakao.js.api.key}")
    private String kakaoJsApiKey;

    // 서비스 상세 페이지
    @GetMapping("/service/{id}/{category}")
    public String serviceDetail(@PathVariable Long id,
                                @PathVariable String category,
                                Model model,
                                @ModelAttribute(value = "message", binding = false) String message,
                                @ModelAttribute(value = "error", binding = false) String error) {
        System.out.println("serviceDetail 호출됨, message=" + message + ", error=" + error);
        ServiceDetailDto serviceDetail = service.getServiceDetail(id);
        System.out.println("recommendCount: " + serviceDetail.getRecommendCount());

        if (serviceDetail == null) {
            return "redirect:/services";
        }
        serviceDetail.setServiceName(StringEscapeUtils.unescapeHtml4(serviceDetail.getServiceName()));
        model.addAttribute("service", serviceDetail);
        model.addAttribute("kakaoJsApiKey", kakaoJsApiKey);

        if ("체육시설".equals(category) || "문화시설".equals(category) || "교육".equals(category) || "진료".equals(category) || "시설대관".equals(category)) {
            List<Article> filteredArticles = articleRepository.findByCategory(category);
            model.addAttribute("articles", filteredArticles);

            // 좌표값도 뷰에 전달 (x, y 좌표명은 dto 필드명에 맞춰 조정)
            model.addAttribute("xCoordinate",
                    serviceDetail.getXCoordinate() != null ? serviceDetail.getXCoordinate() : "127.121303");
            model.addAttribute("yCoordinate",
                    serviceDetail.getYCoordinate() != null ? serviceDetail.getYCoordinate() : "37.385941");
            return "articles/servicedetail";
        }

        return "redirect:/services";
    }

    @GetMapping("/service/{category}")
    public String listByCategoryOnly(@PathVariable String category, Model model) {
        // id 없이 카테고리만으로 조회 시
        // 전체 목록 조회 또는 카테고리별 필터링
        if ("체육시설".equals(category)) {
            return "articles/sportlist";
        } else if ("문화시설".equals(category)) {
            return "articles/culturelist";
        } else if ("교육".equals(category)) {
            return "articles/edulist";
        } else if ("진료".equals(category)) {
            return "articles/medlist";
        } else if ("시설대관".equals(category)) {
            return "articles/rentlist";
        }

        return "articles/all";
    }


    // 카테고리별 서비스 목록 (AJAX 용)
    @GetMapping("/api/services/category/{category}")
    @ResponseBody
    public PageResultDto<ServiceSummaryDto> getServicesByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return service.getServicesByCategory(category, page, size);
    }

    // 지역별 서비스 목록 (AJAX 용)
    @GetMapping("/api/services/area/{area}")
    @ResponseBody
    public PageResultDto<ServiceSummaryDto> getServicesByArea(
            @PathVariable String area,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return service.getServicesByArea(area, page, size);
    }

    // 카테고리별 지역 목록 조회 (AJAX 용)
    @GetMapping("/api/areas/category/{category}")
    @ResponseBody
    public List<String> getAreasByCategory(@PathVariable String category) {
        return service.getAreasByCategory(category);
    }

    // 검색 자동완성 (AJAX 용)
    @GetMapping("/api/search")
    @ResponseBody
    public PageResultDto<ServiceSummaryDto> searchServices(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return service.searchServices(keyword, page, size);
    }

    @PostMapping("/service/{id}/{category}/recommend")
    @ResponseBody
    public Map<String, Object> recommendAjax(
            @PathVariable Long id,
            @PathVariable String category,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Map<String, Object> result = new HashMap<>();
        try {
            int newCount = service.recommendService(id, userDetails.getUser());
            result.put("success", true);
            result.put("message", "추천 완료!");
            result.put("newCount", newCount); // 새 추천수 응답
        } catch (IllegalStateException e) {
            result.put("success", false);
            result.put("message", "이미 추천한 게시물입니다.");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "추천 중 오류가 발생했습니다.");
        }
        return result;
    }

/*
    // 통계 정보 조회 (AJAX 용)
    @GetMapping("/api/statistics")
    @ResponseBody
    public StatisticsDto getStatistics() {
        StatisticsDto statistics = new StatisticsDto();
        //statistics.setCategoryStats(service.getCategoryStatistics());
        //statistics.setAreaStats(service.getAreaStatistics());
        //statistics.setTotalCount(service.getTotalServiceCount());

        return statistics;
    }

    // 통계 정보를 담는 DTO
    public static class StatisticsDto {
        private List<CategoryStatDto> categoryStats;
        private List<AreaStatDto> areaStats;
        private long totalCount;

        // getters and setters
        public List<CategoryStatDto> getCategoryStats() { return categoryStats; }
        public void setCategoryStats(List<CategoryStatDto> categoryStats) { this.categoryStats = categoryStats; }

        public List<AreaStatDto> getAreaStats() { return areaStats; }
        public void setAreaStats(List<AreaStatDto> areaStats) { this.areaStats = areaStats; }

        public long getTotalCount() { return totalCount; }
        public void setTotalCount(long totalCount) { this.totalCount = totalCount; }
    }*/
}

