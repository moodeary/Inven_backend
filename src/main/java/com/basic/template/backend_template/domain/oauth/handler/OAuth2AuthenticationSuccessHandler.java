package com.basic.template.backend_template.domain.oauth.handler;

import com.basic.template.backend_template.domain.oauth.entity.CustomOAuth2User;
import com.basic.template.backend_template.common.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        if (authentication.getPrincipal() instanceof CustomOAuth2User customUser) {
            String token = jwtUtil.generateAccessToken(customUser.getUser().getEmail(), customUser.getUser().getId());

            String redirectUrl = String.format("http://localhost:5173/auth/callback?token=%s",
                    URLEncoder.encode(token, StandardCharsets.UTF_8));

            log.info("OAuth2 로그인 성공: 사용자 ID = {}, 리디렉션 URL = {}",
                    customUser.getUser().getId(), redirectUrl);

            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        } else {
            log.error("OAuth2 로그인 실패: CustomOAuth2User가 아닙니다.");
            getRedirectStrategy().sendRedirect(request, response, "http://localhost:5173/login?error=oauth_failed");
        }
    }
}