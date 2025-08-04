package com.choosedate.security;

import com.choosedate.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        System.out.println("📩 요청 Authorization 헤더: " + authHeader);

        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("🔑 추출된 토큰: " + token);

            if(jwtUtil.validateToken(token)) {
                System.out.println("✅ 토큰 유효함");

                String username = jwtUtil.getUsername(token);
                System.out.println("👤 토큰에서 추출한 사용자명: " + username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println("📚 사용자 정보 로딩 완료: " + userDetails.getUsername());

                // 생성자 구조에 맞게 정보를 보냄
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 저장 X / 현재 요청이 처리되는 동안만 인증 정보를 보관함(응답 후 휘발됨)
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("🔐 인증 객체 SecurityContextHolder에 설정 완료");
            }
            else {
                System.out.println("❌ 토큰 유효하지 않음 (validateToken 실패)");
            }
        }
        else {
            System.out.println("❗ Authorization 헤더 없음 또는 Bearer 형식 아님");
        }
        filterChain.doFilter(request, response);
    }
}
