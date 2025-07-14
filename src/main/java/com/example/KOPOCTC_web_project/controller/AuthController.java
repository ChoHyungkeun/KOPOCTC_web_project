package com.example.KOPOCTC_web_project.controller;

import com.example.KOPOCTC_web_project.dto.LoginRequestDto;
import com.example.KOPOCTC_web_project.dto.SignupRequestDto;
import com.example.KOPOCTC_web_project.dto.TokenResponseDto;
import com.example.KOPOCTC_web_project.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public String signup(@ModelAttribute SignupRequestDto dto) {
        try {
            authService.signup(dto);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            return "redirect:/join?error=true";
        }
    }

    @PostMapping("/signin")
    public String login(@ModelAttribute LoginRequestDto dto, HttpServletResponse response) {
        try {
            TokenResponseDto tokens = authService.login(dto);

            Cookie accessCookie = new Cookie("accessToken", tokens.getAccessToken());
            accessCookie.setHttpOnly(true);
            accessCookie.setPath("/");
            accessCookie.setSecure(false); // 개발환경일 때 false로 설정
            accessCookie.setMaxAge(60 * 60); // 1시간
            response.addCookie(accessCookie);

            Cookie refreshCookie = new Cookie("refreshToken", tokens.getRefreshToken());
            refreshCookie.setHttpOnly(true);
            refreshCookie.setPath("/");
            refreshCookie.setSecure(false);
            refreshCookie.setMaxAge(3 * 60 * 60); // 3시간
            response.addCookie(refreshCookie);

            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login?error=true";

        }

    }

    @GetMapping("/auth-check")
    public ResponseEntity<?> check(@AuthenticationPrincipal UserDetails user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "articles/accessDenied";
    }
}
