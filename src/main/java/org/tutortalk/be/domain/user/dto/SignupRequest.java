package org.tutortalk.be.domain.user.dto;

import java.time.LocalDate;

public record SignupRequest(
        String name,
        String email,            // 이메일 (로그인 ID)
        String password,         // 비밀번호
        LocalDate birthDate,      // 생년월일
        String phone,
        String region

) {}
