package org.tutortalk.be.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.tutortalk.be.domain.user.dto.LoginRequest;
import org.tutortalk.be.domain.user.dto.LoginResponse;
import org.tutortalk.be.domain.user.dto.SignupRequest;
import org.tutortalk.be.domain.user.service.AuthService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
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
}
