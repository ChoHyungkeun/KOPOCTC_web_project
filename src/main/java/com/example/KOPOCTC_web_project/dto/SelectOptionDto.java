package com.example.KOPOCTC_web_project.dto;

public class SelectOptionDto {
    private String value;
    private String label;
    private boolean selected;

    public SelectOptionDto(String value, String label, boolean selected) {
        this.value = value;
        this.label = label;
        this.selected = selected;
    }

    public String getValue() { return value; }
    public String getLabel() { return label; }
    public boolean isSelected() { return selected; }
}

