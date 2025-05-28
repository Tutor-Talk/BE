package org.tutortalk.be.domain.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tutortalk.be.domain.user.dto.AdditionalInfoRequest;
import org.tutortalk.be.domain.user.dto.LoginRequest;
import org.tutortalk.be.domain.user.dto.LoginResponse;
import org.tutortalk.be.domain.user.dto.SignupRequest;
import org.tutortalk.be.domain.user.service.AuthService;
import org.tutortalk.be.global.util.UserPrincipal;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Validated @RequestBody SignupRequest request){
        authService.signup(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest request){
        LoginResponse token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    @PatchMapping("/additional-info")
    public ResponseEntity<Void> updateAdditionalInfo(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody AdditionalInfoRequest dto){
        log.info("[Controller] updateAdditionalInfo 호출됨. userPrincipal id: {}, email: {}", userPrincipal.getId(), userPrincipal.getEmail());
        authService.updateAdditionalInfo(userPrincipal.getId(),dto);
        return ResponseEntity.ok().build();
    }
}
