package com.example.KOPOCTC_web_project.controller;

import com.example.KOPOCTC_web_project.dto.LoginRequestDto;
import com.example.KOPOCTC_web_project.dto.SignupRequestDto;
import com.example.KOPOCTC_web_project.dto.TokenResponseDto;
import com.example.KOPOCTC_web_project.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
            refreshCookie.setMaxAge(2 * 60 * 60); // 2시간
            response.addCookie(refreshCookie);

            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/login?error=true";

        }

    }
}
