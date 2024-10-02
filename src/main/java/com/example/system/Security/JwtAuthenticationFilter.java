package com.example.system.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // 打印前端請求的 URL
        String requestUrl = request.getRequestURL().toString();
        logger.info("Incoming request URL: {}", requestUrl);

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        try {
            // 檢查 Authorization header 是否存在
            if (authHeader == null || authHeader.trim().isEmpty()) {
                logger.warn("Authorization header is missing or empty");
                filterChain.doFilter(request, response);
                return;
            }

            // 檢查是否以 'Bearer ' 開頭
            if (!authHeader.startsWith("Bearer ")) {
                logger.warn("Authorization header does not start with 'Bearer '");
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7); // 獲取 JWT

            // 檢查 JWT 是否由 JwtService 使用正確的 secret key 簽發
            if (!jwtService.isTokenValid(jwt)) {
                logger.warn("JWT is not valid or not signed with the correct secret key");
                filterChain.doFilter(request, response);
                return;
            }

            userEmail = jwtService.extractEmail(jwt); // 從 JWT 中提取 email

            if (userEmail == null) {
                logger.warn("Failed to extract email from JWT token");
                filterChain.doFilter(request, response);
                return;
            }

            // 檢查 SecurityContext 中是否已經存在認證
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                logger.warn("Authentication is already set in the security context");
                filterChain.doFilter(request, response);
                return;
            }

            // 載入使用者資訊
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (userDetails == null) {
                logger.error("UserDetailsService failed to load user by email: {}", userEmail);
                filterChain.doFilter(request, response);
                return;
            }

            // 創建 UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken); // 設置認證資訊

        } catch (Exception e) {
            logger.error("JWT authentication filter failed", e); // 使用 Throwable 參數
        }

        filterChain.doFilter(request, response);
    }
}
