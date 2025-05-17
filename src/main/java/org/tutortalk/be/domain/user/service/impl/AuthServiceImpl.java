package org.tutortalk.be.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tutortalk.be.domain.user.dto.LoginRequest;
import org.tutortalk.be.domain.user.dto.LoginResponse;
import org.tutortalk.be.domain.user.dto.SignupRequest;
import org.tutortalk.be.domain.user.entity.User;
import org.tutortalk.be.domain.user.repository.UserRepository;
import org.tutortalk.be.domain.user.service.AuthService;
import org.tutortalk.be.global.config.JwtProvider;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public void signup(SignupRequest request){

        if(userRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        if (!request.password().equals(request.passwordConfirm())) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        User user = User.builder()
                .lastName(request.lastName())
                .firstName(request.firstName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .birthDate(request.birthDate())
                .build();

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
        String token = jwtProvider.generateToken(request.email());
        return new LoginResponse(token);
    }
}
