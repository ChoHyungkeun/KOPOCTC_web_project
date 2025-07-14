package com.example.KOPOCTC_web_project.service;

import com.example.KOPOCTC_web_project.dto.LoginRequestDto;
import com.example.KOPOCTC_web_project.dto.SignupRequestDto;
import com.example.KOPOCTC_web_project.dto.TokenResponseDto;
import com.example.KOPOCTC_web_project.entity.User;
import com.example.KOPOCTC_web_project.repository.AccessTokenRepository;
import com.example.KOPOCTC_web_project.repository.RefreshTokenRepository;
import com.example.KOPOCTC_web_project.repository.UserRepository;
import com.example.KOPOCTC_web_project.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(SignupRequestDto dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }
        String encodedPw = passwordEncoder.encode(dto.getPassword());
        User user = dto.toEntity(encodedPw);
        userRepository.save(user);
    }

    public TokenResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByUsername(dto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername());

        refreshTokenRepository.save(user.getUsername(), refreshToken, Duration.ofHours(2));

        /*
        String token = jwtUtil.createToken(user.getUsername(), user.getRole());
        return new TokenResponseDto(token); */
        return new TokenResponseDto(accessToken, refreshToken);
    }

    public TokenResponseDto reissueToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        String username = jwtUtil.parseToken(refreshToken).getSubject(); // 토큰에서 사용자이름 추출
        String savedRefreshToken = refreshTokenRepository.findById(username).orElseThrow(() ->
                new IllegalArgumentException("로그아웃 되었거나 유효하지 않은 토큰입니다."));
        if (!savedRefreshToken.equals(refreshToken)) {
            throw new IllegalArgumentException("토큰 정보가 일치하지 않습니다.");
        }

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("사용자 없음"));

        String newAccessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());

        return new TokenResponseDto(newAccessToken, refreshToken);
    }

    public void logout(String username, String accessToken) {
        System.out.println("AuthService.logout 호출: username=" + username + ", accessToken=" + accessToken);
        refreshTokenRepository.deleteById(username);
        if (accessToken != null) {
            accessTokenRepository.saveBlackList(accessToken, Duration.ofHours(1));
            System.out.println("블랙리스트에 accessToken 저장 완료");
        }
    }
}
