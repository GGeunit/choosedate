package com.choosedate.controller;

import com.choosedate.domain.dto.LoginRequestDto;
import com.choosedate.domain.dto.LoginResponseDto;
import com.choosedate.domain.dto.UserRequestDto;
import com.choosedate.domain.dto.UserResponseDto;
import com.choosedate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto request) {
        return ResponseEntity.ok(userService.saveUser(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(userService.login(request));
    }
}
