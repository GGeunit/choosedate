package com.choosedate.controller;

import com.choosedate.domain.dto.RefreshRequestDto;
import com.choosedate.domain.dto.TokenResponseDto;
import com.choosedate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/refresh")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequestDto request) {
        try {
            String newAccessToken = userService.refreshAccessToken(request.getRefreshToken());
            // return ResponseEntity.ok(newAccessToken); → 확장성(유지보수)에 좋지 않음
            return ResponseEntity.ok(new TokenResponseDto(newAccessToken)); // 클라이언트에게 명확한 JSON 구조로 전달
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 Refresh Token입니다.");
        }
    }
}
