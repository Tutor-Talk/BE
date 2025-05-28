package org.tutortalk.be.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tutortalk.be.domain.user.dto.AdditionalInfoRequest;
import org.tutortalk.be.domain.user.dto.LoginRequest;
import org.tutortalk.be.domain.user.dto.LoginResponse;
import org.tutortalk.be.domain.user.dto.SignupRequest;
import org.tutortalk.be.domain.user.entity.User;
import org.tutortalk.be.domain.user.repository.UserRepository;
import org.tutortalk.be.domain.user.service.AuthService;
import org.tutortalk.be.global.config.JwtProvider;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public void signup(SignupRequest request){

        if(userRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .birthDate(request.birthDate())
                .phone(request.phone())
                .region(request.region())
                .googleId(null)
                .profileImage(null)
                .build();

        user.setIsProfileComplete(true);
        userRepository.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request){
        //spring security를 통해 인증 시도
        // 이 방법 이용하면 403 forbidden 오류 발생 수정해야 함
        /*authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );*/

        // Service 레이어에서 수동으로 인증 (직접 DB 조회 + 비밀번호 비교)
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다");
        }

        //인증 성공 시 JWT토큰 생성
        String token = jwtProvider.generateToken(request.email(),user.isProfileComplete());
        return new LoginResponse(token);
    }

    @Override
    public void updateAdditionalInfo(Long userId, AdditionalInfoRequest dto) {
        log.info("[Service] updateAdditionalInfo 호출됨. userId: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 없음"));

        user.updateProfile(
                dto.name(),
                dto.phone(),
                dto.region(),
                dto.birthDate()
        );

        userRepository.save(user);
        log.info("[Service] 사용자 정보 업데이트 완료. userId: {}", userId);
    }
}
