package com.basic.template.backend_template.domain.user.service;

import com.basic.template.backend_template.domain.user.dto.JwtResDto;
import com.basic.template.backend_template.domain.user.dto.LoginReqDto;
import com.basic.template.backend_template.domain.user.dto.SignupReqDto;
import com.basic.template.backend_template.domain.user.dto.UserResDto;

public interface UserService {

    UserResDto signup(SignupReqDto request);

    JwtResDto login(LoginReqDto request);

    UserResDto getUserByEmail(String email);

    UserResDto getUserById(Long userId);
}