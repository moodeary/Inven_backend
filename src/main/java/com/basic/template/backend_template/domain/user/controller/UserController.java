package com.basic.template.backend_template.domain.user.controller;

import com.basic.template.backend_template.common.dto.ResponseApi;
import com.basic.template.backend_template.common.exception.BusinessException;
import com.basic.template.backend_template.common.util.SecurityUtil;
import com.basic.template.backend_template.domain.user.dto.JwtResDto;
import com.basic.template.backend_template.domain.user.dto.LoginReqDto;
import com.basic.template.backend_template.domain.user.dto.SignupReqDto;
import com.basic.template.backend_template.domain.user.dto.UserResDto;
import com.basic.template.backend_template.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseApi<UserResDto>> signup(@Valid @RequestBody SignupReqDto request) {
        try {
            UserResDto response = userService.signup(request);
            return ResponseEntity.ok(ResponseApi.success(response, "회원가입이 완료되었습니다."));
        } catch (BusinessException e) {
            log.warn("회원가입 실패: {}, 이메일: {}", e.getMessage(), request.getEmail());
            return ResponseEntity.status(e.getStatus())
                    .body(ResponseApi.error(e.getMessage(), e.getErrorCode()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseApi<JwtResDto>> login(@Valid @RequestBody LoginReqDto request) {
        try {
            JwtResDto response = userService.login(request);
            log.info("로그인 성공: {}", request.getEmail());
            return ResponseEntity.ok(ResponseApi.success(response, "로그인이 완료되었습니다."));
        } catch (BusinessException e) {
            log.warn("로그인 실패: {}, 이메일: {}", e.getMessage(), request.getEmail());
            return ResponseEntity.status(e.getStatus())
                    .body(ResponseApi.error(e.getMessage(), e.getErrorCode()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseApi<UserResDto>> getCurrentUser() {
        String email = SecurityUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ResponseApi.error("인증이 필요합니다.", "UNAUTHORIZED"));
        }

        try {
            UserResDto response = userService.getUserByEmail(email);
            return ResponseEntity.ok(ResponseApi.success(response, "현재 사용자 정보를 조회했습니다."));
        } catch (BusinessException e) {
            log.warn("사용자 조회 실패: {}, 이메일: {}", e.getMessage(), email);
            return ResponseEntity.status(e.getStatus())
                    .body(ResponseApi.error(e.getMessage(), e.getErrorCode()));
        }
    }
}