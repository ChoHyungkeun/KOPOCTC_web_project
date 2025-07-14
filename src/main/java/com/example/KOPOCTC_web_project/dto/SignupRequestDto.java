package com.example.KOPOCTC_web_project.dto;

import com.example.KOPOCTC_web_project.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    private String username;
    private String password;
    private String role;

    public User toEntity(String encodedPw) {
        return User.builder()
                .username(username)
                .password(encodedPw)
                .role(this.role != null? this.role : "USER")
                .build();
    }
}
