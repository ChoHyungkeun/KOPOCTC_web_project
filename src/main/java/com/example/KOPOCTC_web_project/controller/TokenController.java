package com.example.KOPOCTC_web_project.controller;

import com.example.KOPOCTC_web_project.dto.TokenResponseDto;
import com.example.KOPOCTC_web_project.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {
    private final AuthService authService;

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponseDto> reissue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("/reissue 컨트롤러 진입");
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    System.out.println("리프레시 토큰 수신: " + cookie.getValue());
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        TokenResponseDto tokenResponseDto = authService.reissueToken(refreshToken);

        Cookie accessCookie = new Cookie("accessToken", tokenResponseDto.getAccessToken());
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setSecure(false);
        accessCookie.setMaxAge(60 * 60);
        response.addCookie(accessCookie);

        return ResponseEntity.ok(tokenResponseDto);
    }
}
