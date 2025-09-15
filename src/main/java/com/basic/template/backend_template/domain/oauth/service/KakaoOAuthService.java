package com.basic.template.backend_template.domain.oauth.service;

import com.basic.template.backend_template.domain.user.entity.User;
import com.basic.template.backend_template.domain.user.entity.UserRole;
import com.basic.template.backend_template.domain.user.repository.UserRepository;
import com.basic.template.backend_template.domain.oauth.dto.KakaoUserInfo;
import com.basic.template.backend_template.domain.oauth.entity.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoOAuthService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(oAuth2User.getAttributes());

        User user = saveOrUpdateUser(kakaoUserInfo);

        return new CustomOAuth2User(user, oAuth2User.getAttributes(), userNameAttributeName);
    }

    private User saveOrUpdateUser(KakaoUserInfo kakaoUserInfo) {
        return userRepository.findByProviderAndProviderId("kakao", kakaoUserInfo.getId())
                .map(existingUser -> {
                    log.info("기존 카카오 사용자 로그인: {}", existingUser.getEmail());
                    return existingUser;
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(kakaoUserInfo.getEmail())
                            .nickname(kakaoUserInfo.getNickname())
                            .role(UserRole.USER)
                            .provider("kakao")
                            .providerId(kakaoUserInfo.getId())
                            .build();
                    log.info("신규 카카오 사용자 생성: {}", newUser.getEmail());
                    return userRepository.save(newUser);
                });
    }
}