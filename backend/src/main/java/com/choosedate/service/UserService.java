package com.choosedate.service;

import com.choosedate.domain.dto.LoginRequestDto;
import com.choosedate.domain.dto.LoginResponseDto;
import com.choosedate.domain.dto.UserRequestDto;
import com.choosedate.domain.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    public UserResponseDto saveUser(UserRequestDto request);
    public List<UserResponseDto> getAllUsers();
    public LoginResponseDto login(LoginRequestDto request);
    public String refreshAccessToken(String refreshToken);
}
