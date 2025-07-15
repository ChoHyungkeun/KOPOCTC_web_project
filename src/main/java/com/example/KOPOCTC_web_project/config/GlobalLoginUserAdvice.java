package com.example.KOPOCTC_web_project.config;

import com.example.KOPOCTC_web_project.entity.User;
import com.example.KOPOCTC_web_project.security.CustomUserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@ControllerAdvice
public class GlobalLoginUserAdvice {

    @ModelAttribute("loginUser")
    public User addLoginUserToModel(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            System.out.println("loginUser: " + userDetails.getUser());
            return userDetails.getUser();  // CustomUserDetails가 감싼 실제 User 객체 반환
        }
        System.out.println("loginUser: null");
        return null;
    }
}

