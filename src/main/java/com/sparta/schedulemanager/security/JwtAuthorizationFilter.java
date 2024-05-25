package com.sparta.schedulemanager.security;

import com.sparta.schedulemanager.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (StringUtils.hasText(tokenValue)) {

            if (!jwtUtil.validateAccessToken(tokenValue)) {
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            // 액세스 토큰 만료 시간 확인
            Date expiration = info.getExpiration();
            Date now = new Date();
            if (expiration != null && expiration.before(now)) { // 액세스 토큰 만료 여부 파악
                Cookie[] cookies = req.getCookies();
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if ("refreshToken".equals(cookie.getName())) {
                            String refreshToken = cookie.getValue();
                            if (!jwtUtil.validateRefreshToken(refreshToken)) {
                                log.error("Token Error");
                                return;
                            }
                            // 새로운 액세스 토큰 생성
                            String newAccessToken = jwtUtil.createAccessTokenFromRefreshToken(refreshToken);
                            // 새로 생성된 액세스 토큰을 헤더에 추가
                            res.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
                            break;
                        }
                    }
                }
            }

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
