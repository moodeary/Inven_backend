package com.basic.template.backend_template.domain.user.controller;

import com.basic.template.backend_template.common.util.SecurityUtil;
import com.basic.template.backend_template.domain.user.dto.JwtResDto;
import com.basic.template.backend_template.domain.user.dto.LoginReqDto;
import com.basic.template.backend_template.domain.user.dto.SignupReqDto;
import com.basic.template.backend_template.domain.user.dto.UserResDto;
import com.basic.template.backend_template.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResDto> signup(@Valid @RequestBody SignupReqDto request) {
        UserResDto response = userService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResDto> login(@Valid @RequestBody LoginReqDto request) {
        JwtResDto response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResDto> getCurrentUser() {
        String email = SecurityUtil.getCurrentUserEmail();
        if (email == null) {
            return ResponseEntity.status(401).build();
        }

        UserResDto response = userService.getUserByEmail(email);
        return ResponseEntity.ok(response);
    }
}