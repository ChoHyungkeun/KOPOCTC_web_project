package com.example.KOPOCTC_web_project.controller;

import com.example.KOPOCTC_web_project.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RequiredArgsConstructor
@Controller
public class LoginController {
    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        return "articles/loginPage";
    }

    @GetMapping("/join")
    public String signupPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "이미 존재하는 아이디입니다.");
        }
        return "articles/signupPage";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,
                         @AuthenticationPrincipal UserDetails userDetails) {

        String accessToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }
        // 서버에 저장된 리프레시 토큰 제거 (예: Redis)
        if (userDetails != null && accessToken != null) {
            System.out.println("로그아웃: username=" + userDetails.getUsername() + ", accessToken=" + accessToken);
            String username = userDetails.getUsername();
            authService.logout(username, accessToken); // accessToken, refreshToken 삭제
        } else if (userDetails != null) {
            authService.logout(userDetails.getUsername(), null); // RefreshToken만 삭제
            System.out.println("로그아웃: userDetails or accessToken is null");
        }


        // 클라이언트 쿠키 제거
        Cookie accessCookie = new Cookie("accessToken", null);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(0); // 삭제

        Cookie refreshCookie = new Cookie("refreshToken", null);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(0); // 삭제

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        return "redirect:/login";
    }
}
