package com.example.KOPOCTC_web_project.dto;

public class PageNumberDto {
    private int page;       // 0-based page 번호
    private boolean current;

    public PageNumberDto(int page, boolean current) {
        this.page = page;
        this.current = current;
    }

    public int getPage() {
        return page;
    }

    public boolean isCurrent() {
        return current;
    }

    public int getPagePlusOne() {
        return page + 1;
    }  // UI용 (1-based)
}
