package com.basic.template.backend_template.domain.oauth.controller;

import com.basic.template.backend_template.domain.user.dto.JwtResDto;
import com.basic.template.backend_template.domain.user.dto.UserResDto;
import com.basic.template.backend_template.domain.oauth.entity.CustomOAuth2User;
import com.basic.template.backend_template.common.util.JwtUtil;
import com.basic.template.backend_template.common.dto.ResponseApi;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtUtil jwtUtil;

    @GetMapping("/kakao/callback")
    public void kakaoCallback(@AuthenticationPrincipal OAuth2User oauth2User, HttpServletResponse response) throws IOException {
        if (oauth2User instanceof CustomOAuth2User customUser) {
            String token = jwtUtil.generateAccessToken(customUser.getUser().getEmail(), customUser.getUser().getId());

            String redirectUrl = String.format("http://localhost:5173/auth/callback?token=%s", token);
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("http://localhost:5173/login?error=oauth_failed");
        }
    }

    @GetMapping("/kakao/login")
    public ResponseEntity<ResponseApi<JwtResDto>> kakaoLogin(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User instanceof CustomOAuth2User customUser) {
            String token = jwtUtil.generateAccessToken(customUser.getUser().getEmail(), customUser.getUser().getId());
            UserResDto userResDto = UserResDto.from(customUser.getUser());
            JwtResDto response = JwtResDto.of(token, userResDto);
            log.info("카카오 OAuth 로그인 성공: {}", customUser.getUser().getEmail());
            return ResponseEntity.ok(ResponseApi.success(response, "카카오 로그인이 완료되었습니다."));
        } else {
            log.error("OAuth2 인증 실패: CustomOAuth2User가 아닙니다.");
            return ResponseEntity.badRequest()
                    .body(ResponseApi.error("OAuth 인증에 실패했습니다.", "OAUTH_FAILED"));
        }
    }
}