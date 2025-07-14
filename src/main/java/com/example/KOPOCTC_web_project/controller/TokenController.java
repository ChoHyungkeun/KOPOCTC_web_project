package com.example.KOPOCTC_web_project.controller;

import com.example.KOPOCTC_web_project.dto.TokenResponseDto;
import com.example.KOPOCTC_web_project.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(@RequestParam String refreshToken) {
        TokenResponseDto tokenResponseDto = authService.reissueToken(refreshToken);
        return ResponseEntity.ok(tokenResponseDto);
    }
}
