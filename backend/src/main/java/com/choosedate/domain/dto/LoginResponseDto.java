package com.choosedate.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private Long userId;
    private String username;
    private String email;
    private String role;
    private String token;
    private String refreshToken;
}
