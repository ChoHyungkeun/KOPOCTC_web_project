package com.example.KOPOCTC_web_project.config;

import com.example.KOPOCTC_web_project.repository.AccessTokenRepository;
import com.example.KOPOCTC_web_project.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final AccessTokenRepository accessTokenRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {

        String requestURI = req.getRequestURI();

            if (requestURI.equals("/join") || requestURI.equals("/reissue") ||
                requestURI.equals("/login") || requestURI.equals("/signin") ||
                requestURI.equals("/signup") || requestURI.equals("/favicon.ico") ||
                requestURI.startsWith("/css/") || requestURI.startsWith("/js/") ||
                requestURI.startsWith("/images/") || requestURI.equals("/error") ||
                requestURI.startsWith("/h2-console")) {
            chain.doFilter(req, res);
            return;
        }

        String token = null;
        String header = req.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);

        } else {
            if (req.getCookies() != null) {
                for (jakarta.servlet.http.Cookie cookie : req.getCookies()) {
                    if ("accessToken".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }
        if (token != null) {
            if (accessTokenRepository.isBlackListed(token)) {
                System.out.println("AccessToken이 블랙리스트에 존재합니다.");
                SecurityContextHolder.clearContext();
                chain.doFilter(req, res);
                return;
            }
            try {
                Claims claims = jwtUtil.parseToken(token);
                String username = claims.getSubject();

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                System.out.println("SecurityContext에 인증 세팅 완료");
            } catch (ExpiredJwtException e) {
                System.out.println("토큰 만료됨: " + e.getMessage());
            } catch (JwtException e) {
                SecurityContextHolder.clearContext();
                System.out.println("유효하지 않은 토큰: " + e.getMessage());
            } catch (UsernameNotFoundException e) {
                SecurityContextHolder.clearContext();
                System.out.println("유저를 찾을 수 없음. 쿠키 삭제 필요.");
            }
        } else {
            System.out.println("토큰이 없음");
        }
        chain.doFilter(req, res);
    }
}
