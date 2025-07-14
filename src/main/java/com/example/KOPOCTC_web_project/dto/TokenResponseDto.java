package com.example.KOPOCTC_web_project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

}
