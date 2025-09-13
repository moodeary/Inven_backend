package com.basic.template.backend_template.domain.user.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResDto {

    private String accessToken;
    private String tokenType;
    private UserResDto user;

    public static JwtResDto of(String accessToken, UserResDto user) {
        return JwtResDto.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .user(user)
                .build();
    }
}