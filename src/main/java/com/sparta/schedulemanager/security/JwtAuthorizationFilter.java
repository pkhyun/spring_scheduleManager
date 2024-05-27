package com.sparta.schedulemanager.security;

import com.sparta.schedulemanager.entity.UserRoleEnum;
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
                // 토큰이 만료된 경우 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급
                String refreshToken = jwtUtil.getRefreshTokenFromCookie(req);
                if (StringUtils.hasText(refreshToken)) {
                    Claims refreshTokenInfo = jwtUtil.getUserInfoFromToken(refreshToken);
                    String username = refreshTokenInfo.getSubject();
                    if (StringUtils.hasText(username)) {
                        // 새로운 액세스 토큰 발급
                        String roleString = refreshTokenInfo.get(JwtUtil.AUTHORIZATION_KEY, String.class);
                        UserRoleEnum role = null;
                        if (roleString.equals(UserRoleEnum.Authority.USER)) {
                            role = UserRoleEnum.USER;
                        } else if (roleString.equals(UserRoleEnum.Authority.ADMIN)) {
                            role = UserRoleEnum.ADMIN;
                        }
                        String newAccessToken = jwtUtil.createToken(username, role);
                        res.addHeader(JwtUtil.AUTHORIZATION_HEADER, newAccessToken); // 새로운 액세스 토큰을 헤더에 추가
                        log.info("새로 발행한 액세스 토큰: {}", newAccessToken);
                    }
                }
            } else {
                Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
                try {
                    setAuthentication(info.getSubject());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return;
                }
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
