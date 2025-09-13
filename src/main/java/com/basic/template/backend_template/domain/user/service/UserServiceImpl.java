package com.basic.template.backend_template.domain.user.service;

import com.basic.template.backend_template.common.exception.BusinessException;
import com.basic.template.backend_template.common.util.JwtUtil;
import com.basic.template.backend_template.domain.user.dto.*;
import com.basic.template.backend_template.domain.user.entity.User;
import com.basic.template.backend_template.domain.user.entity.UserRole;
import com.basic.template.backend_template.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserResDto signup(SignupReqDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw BusinessException.conflict("이미 사용 중인 이메일입니다.");
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw BusinessException.conflict("이미 사용 중인 닉네임입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .role(UserRole.USER)
                .build();

        User savedUser = userRepository.save(user);
        return UserResDto.from(savedUser);
    }

    public JwtResDto login(LoginReqDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> BusinessException.unauthorized("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw BusinessException.unauthorized("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getId());
        UserResDto userResponse = UserResDto.from(user);

        return JwtResDto.of(accessToken, userResponse);
    }

    @Transactional(readOnly = true)
    public UserResDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> BusinessException.notFound("사용자를 찾을 수 없습니다."));
        return UserResDto.from(user);
    }

    @Transactional(readOnly = true)
    public UserResDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> BusinessException.notFound("사용자를 찾을 수 없습니다."));
        return UserResDto.from(user);
    }
}