package com.choosedate.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;


import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key key; // 비밀키

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Access Token 생성
    public String generateToken(String username) {
        long accessTokenValidTime = 1000L * 60 * 15; // 15분

        // 사용자 이름(subject)뿐 아니라 필요한 추가 정보들을 넣을 수 있음
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256) // 비밀키로 서명(토큰 위조 방지)
                .compact(); // 토큰을 문자열로 완성
    }

    // Refresh Token 생성(Access Token이 만료되면 Refresh Token으로 발급 가능)
    public String generateRefreshToken(String username) {
        long refreshTokenValidTime = 1000L * 60 * 60 * 24 * 7; // 7일
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenValidTime);

        return Jwts.builder()
                // Refresh Token은 수명이 길어 노출되면 위험하기 때문에
                // 가능한 한 민감한 정보는 넣지 않고, 단순하게 만드는 게 보안에 좋음
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰에서 사용자명 추출
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build() // JWT를 해석할 준비를 함(key로 서명 확인도 함)
                .parseClaimsJws(token) // 토큰을 파싱(해석)
                .getBody() // 그 안에 있는 내용(claim)을 가져와서
                .getSubject(); // 거기서 사용자 이름만 꺼냄
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
