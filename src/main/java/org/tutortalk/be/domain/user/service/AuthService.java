package org.tutortalk.be.domain.user.service;

import org.tutortalk.be.domain.user.dto.AdditionalInfoRequest;
import org.tutortalk.be.domain.user.dto.LoginRequest;
import org.tutortalk.be.domain.user.dto.LoginResponse;
import org.tutortalk.be.domain.user.dto.SignupRequest;

public interface AuthService {
    /*
     * @param request 회원가입 요청 DTO
     * */
    void signup(SignupRequest request);

    /*
     * @param request 로그인 요청 DTO
     * @return 발급된 JWT 토큰
     * */
    LoginResponse login(LoginRequest request);

    void updateAdditionalInfo(Long userId, AdditionalInfoRequest dto);
}
