package org.tutortalk.be.domain.welfare.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tutortalk.be.domain.welfare.dto.WelfareResponseDto;
import org.tutortalk.be.domain.welfare.service.WelfareService;
import org.tutortalk.be.global.util.UserPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/welfare")
@RequiredArgsConstructor
public class WelfareController {
    private final WelfareService welfareService;

    private static final Logger log = LoggerFactory.getLogger(WelfareController.class);

    @GetMapping("/list")
    public ResponseEntity<List<WelfareResponseDto>> list(@AuthenticationPrincipal UserPrincipal principal) {
        String email = principal.getEmail(); // 인증된 사용자 ID(이메일)
        log.info("로그인한 사용자의 이메일: {}", email);

        List<WelfareResponseDto> services = welfareService.getServicesForUser(email);
        return ResponseEntity.ok(services);
    }
}

