package com.choosedate.service;

import com.choosedate.domain.User;
import com.choosedate.domain.dto.LoginRequestDto;
import com.choosedate.domain.dto.LoginResponseDto;
import com.choosedate.domain.dto.UserRequestDto;
import com.choosedate.domain.dto.UserResponseDto;
import com.choosedate.repository.UserRepository;
import com.choosedate.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponseDto saveUser(UserRequestDto request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .email(request.getEmail())
                .role("ROLE_USER")
                .build();

        User saved = userRepository.save(user);

        return toResponseDto(saved);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

        // 입력한 비밀번호가 저장된 비밀번호가 다를 때 0 리턴
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        user.setRefreshToken(refreshToken);
        userRepository.save(user); // update

        return LoginResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        // 1. 토큰 파싱(refreshToken에서 username 추출)
        String username = jwtUtil.getUsername(refreshToken);

        // 2. 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

        // 3. 저장된 refreshToken과 비교
        if(!refreshToken.equals(user.getRefreshToken())) {
            throw new IllegalArgumentException("Refresh Token 불일치");
        }
        
        // 4. 일치 시 새로운 Access Token 발급
        return jwtUtil.generateToken(username);
    }

    private UserResponseDto toResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
