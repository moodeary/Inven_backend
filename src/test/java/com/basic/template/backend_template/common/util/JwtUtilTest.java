package com.basic.template.backend_template.common.util;

import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JwtUtil 테스트")
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private String testEmail;
    private Long testUserId;

    @BeforeEach
    void setUp() {
        String secretKey = "myTestSecretKey123456789012345678901234567890";
        long accessTokenExpiration = 3600000; // 1시간
        jwtUtil = new JwtUtil(secretKey, accessTokenExpiration);

        testEmail = "test@example.com";
        testUserId = 1L;
    }

    @Test
    @DisplayName("액세스 토큰 생성 성공")
    void generateAccessToken_Success() {
        // when
        String token = jwtUtil.generateAccessToken(testEmail, testUserId);

        // then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT는 세 부분으로 구성
    }

    @Test
    @DisplayName("토큰에서 이메일 추출 성공")
    void extractEmail_Success() {
        // given
        String token = jwtUtil.generateAccessToken(testEmail, testUserId);

        // when
        String extractedEmail = jwtUtil.extractEmail(token);

        // then
        assertThat(extractedEmail).isEqualTo(testEmail);
    }

    @Test
    @DisplayName("토큰에서 사용자 ID 추출 성공")
    void extractUserId_Success() {
        // given
        String token = jwtUtil.generateAccessToken(testEmail, testUserId);

        // when
        Long extractedUserId = jwtUtil.extractUserId(token);

        // then
        assertThat(extractedUserId).isEqualTo(testUserId);
    }

    @Test
    @DisplayName("유효한 토큰 검증 성공")
    void isTokenValid_Success() {
        // given
        String token = jwtUtil.generateAccessToken(testEmail, testUserId);

        // when
        boolean isValid = jwtUtil.isTokenValid(token);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("잘못된 토큰 검증 실패")
    void isTokenValid_Fail_InvalidToken() {
        // given
        String invalidToken = "invalid.token.here";

        // when
        boolean isValid = jwtUtil.isTokenValid(invalidToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("빈 토큰 검증 실패")
    void isTokenValid_Fail_EmptyToken() {
        // when
        boolean isValid = jwtUtil.isTokenValid("");

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("null 토큰 검증 실패")
    void isTokenValid_Fail_NullToken() {
        // when
        boolean isValid = jwtUtil.isTokenValid(null);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("다른 시크릿 키로 생성된 토큰 검증 실패")
    void isTokenValid_Fail_DifferentSecretKey() {
        // given
        JwtUtil differentJwtUtil = new JwtUtil("differentSecretKey123456789012345678901234567890", 3600000);
        String tokenFromDifferentUtil = differentJwtUtil.generateAccessToken(testEmail, testUserId);

        // when
        boolean isValid = jwtUtil.isTokenValid(tokenFromDifferentUtil);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("잘못된 토큰에서 이메일 추출 실패")
    void extractEmail_Fail_InvalidToken() {
        // given
        String invalidToken = "invalid.token.here";

        // when & then
        assertThatThrownBy(() -> jwtUtil.extractEmail(invalidToken))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("잘못된 토큰에서 사용자 ID 추출 실패")
    void extractUserId_Fail_InvalidToken() {
        // given
        String invalidToken = "invalid.token.here";

        // when & then
        assertThatThrownBy(() -> jwtUtil.extractUserId(invalidToken))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("하위 호환성 메서드 - getEmailFromToken 성공")
    void getEmailFromToken_Success() {
        // given
        String token = jwtUtil.generateAccessToken(testEmail, testUserId);

        // when
        String extractedEmail = jwtUtil.getEmailFromToken(token);

        // then
        assertThat(extractedEmail).isEqualTo(testEmail);
    }

    @Test
    @DisplayName("하위 호환성 메서드 - getUserIdFromToken 성공")
    void getUserIdFromToken_Success() {
        // given
        String token = jwtUtil.generateAccessToken(testEmail, testUserId);

        // when
        Long extractedUserId = jwtUtil.getUserIdFromToken(token);

        // then
        assertThat(extractedUserId).isEqualTo(testUserId);
    }

    @Test
    @DisplayName("하위 호환성 메서드 - validateToken 성공")
    void validateToken_Success() {
        // given
        String token = jwtUtil.generateAccessToken(testEmail, testUserId);

        // when
        boolean isValid = jwtUtil.validateToken(token);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("토큰 만료 시간 확인")
    void tokenExpiration_Test() {
        // given
        long shortExpiration = 1000; // 1초
        JwtUtil shortExpirationJwtUtil = new JwtUtil("testSecretKey123456789012345678901234567890", shortExpiration);
        String token = shortExpirationJwtUtil.generateAccessToken(testEmail, testUserId);

        // when - 토큰이 바로 생성되었을 때는 유효해야 함
        boolean isValidImmediately = shortExpirationJwtUtil.isTokenValid(token);

        // then
        assertThat(isValidImmediately).isTrue();

        // 실제 만료 테스트는 시간이 오래 걸리므로 생략
        // 운영 환경에서는 만료 시간을 적절히 설정해야 함
    }
}