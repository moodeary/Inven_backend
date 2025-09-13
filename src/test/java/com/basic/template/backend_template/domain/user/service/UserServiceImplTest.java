package com.basic.template.backend_template.domain.user.service;

import com.basic.template.backend_template.common.exception.BusinessException;
import com.basic.template.backend_template.common.util.JwtUtil;
import com.basic.template.backend_template.domain.user.dto.*;
import com.basic.template.backend_template.domain.user.entity.User;
import com.basic.template.backend_template.domain.user.entity.UserRole;
import com.basic.template.backend_template.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 테스트")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private SignupReqDto signupRequest;
    private LoginReqDto loginRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .nickname("testuser")
                .role(UserRole.USER)
                .build();

        signupRequest = new SignupReqDto();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setNickname("testuser");

        loginRequest = new LoginReqDto();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup_Success() {
        // given
        given(userRepository.existsByEmail(signupRequest.getEmail())).willReturn(false);
        given(userRepository.existsByNickname(signupRequest.getNickname())).willReturn(false);
        given(passwordEncoder.encode(signupRequest.getPassword())).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willReturn(testUser);

        // when
        UserResDto result = userService.signup(signupRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(result.getNickname()).isEqualTo(testUser.getNickname());
        assertThat(result.getRole()).isEqualTo(testUser.getRole().name());

        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository).existsByNickname(signupRequest.getNickname());
        verify(passwordEncoder).encode(signupRequest.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signup_Fail_DuplicateEmail() {
        // given
        given(userRepository.existsByEmail(signupRequest.getEmail())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.signup(signupRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 사용 중인 이메일입니다.");

        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository, never()).existsByNickname(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("회원가입 실패 - 닉네임 중복")
    void signup_Fail_DuplicateNickname() {
        // given
        given(userRepository.existsByEmail(signupRequest.getEmail())).willReturn(false);
        given(userRepository.existsByNickname(signupRequest.getNickname())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.signup(signupRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 사용 중인 닉네임입니다.");

        verify(userRepository).existsByEmail(signupRequest.getEmail());
        verify(userRepository).existsByNickname(signupRequest.getNickname());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // given
        String accessToken = "test.access.token";
        given(userRepository.findByEmail(loginRequest.getEmail())).willReturn(Optional.of(testUser));
        given(passwordEncoder.matches(loginRequest.getPassword(), testUser.getPassword())).willReturn(true);
        given(jwtUtil.generateAccessToken(testUser.getEmail(), testUser.getId())).willReturn(accessToken);

        // when
        JwtResDto result = userService.login(loginRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo(accessToken);
        assertThat(result.getUser().getEmail()).isEqualTo(testUser.getEmail());

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), testUser.getPassword());
        verify(jwtUtil).generateAccessToken(testUser.getEmail(), testUser.getId());
    }

    @Test
    @DisplayName("로그인 실패 - 사용자 없음")
    void login_Fail_UserNotFound() {
        // given
        given(userRepository.findByEmail(loginRequest.getEmail())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이메일 또는 비밀번호가 올바르지 않습니다.");

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtUtil, never()).generateAccessToken(any(), any());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_Fail_WrongPassword() {
        // given
        given(userRepository.findByEmail(loginRequest.getEmail())).willReturn(Optional.of(testUser));
        given(passwordEncoder.matches(loginRequest.getPassword(), testUser.getPassword())).willReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.login(loginRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이메일 또는 비밀번호가 올바르지 않습니다.");

        verify(userRepository).findByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), testUser.getPassword());
        verify(jwtUtil, never()).generateAccessToken(any(), any());
    }

    @Test
    @DisplayName("이메일로 사용자 조회 성공")
    void getUserByEmail_Success() {
        // given
        given(userRepository.findByEmail(testUser.getEmail())).willReturn(Optional.of(testUser));

        // when
        UserResDto result = userService.getUserByEmail(testUser.getEmail());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(result.getNickname()).isEqualTo(testUser.getNickname());

        verify(userRepository).findByEmail(testUser.getEmail());
    }

    @Test
    @DisplayName("이메일로 사용자 조회 실패 - 사용자 없음")
    void getUserByEmail_Fail_UserNotFound() {
        // given
        given(userRepository.findByEmail(testUser.getEmail())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserByEmail(testUser.getEmail()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");

        verify(userRepository).findByEmail(testUser.getEmail());
    }

    @Test
    @DisplayName("ID로 사용자 조회 성공")
    void getUserById_Success() {
        // given
        given(userRepository.findById(testUser.getId())).willReturn(Optional.of(testUser));

        // when
        UserResDto result = userService.getUserById(testUser.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testUser.getId());
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());

        verify(userRepository).findById(testUser.getId());
    }

    @Test
    @DisplayName("ID로 사용자 조회 실패 - 사용자 없음")
    void getUserById_Fail_UserNotFound() {
        // given
        given(userRepository.findById(testUser.getId())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserById(testUser.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");

        verify(userRepository).findById(testUser.getId());
    }
}