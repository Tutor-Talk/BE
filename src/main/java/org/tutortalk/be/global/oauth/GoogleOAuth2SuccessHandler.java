package org.tutortalk.be.global.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.tutortalk.be.domain.user.entity.User;
import org.tutortalk.be.domain.user.repository.UserRepository;
import org.tutortalk.be.global.config.JwtProvider;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // oauth 프로필 추출
        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String profileImage = oAuth2User.getAttribute("picture");

        // 회원가입 여부 확인
        User user = userRepository.findByGoogleId(googleId).orElseGet(() -> {
            User newUser = User.builder()
                    .name("") //기본값
                    .email(email)
                    .password(null)
                    .birthDate(LocalDate.of(1900, 1, 1)) //기본값
                    .phone("") //기본값
                    .region("") //기본값
                    .googleId(googleId)
                    .profileImage(profileImage)
                    .build();
            return userRepository.save(newUser);
        });

        Boolean isProfileComplete = user.isProfileComplete();
        String token = jwtProvider.generateToken(user.getEmail(),isProfileComplete);
        /*
        방법 1. 프론트와 연결 후 리다이렉트 방식으로 변경 프론트에서 토큰 추출 -> 프론트에서 replaceState()를 이용하여 URL에서 제거
        */
        response.sendRedirect("http://localhost:3000/oauth-callback?token=" + token + "&isProfileComplete=" + isProfileComplete);

        //방법 2. 프론트와 연결 후 리다이렉트 방식을 이용하지만 토큰을 Set-Cookie 헤더로 전달
        /*Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setSecure(true); // HTTPS 환경일 때

        Cookie profileCookie = new Cookie("isProfileComplete", isProfileComplete.toString());
        profileCookie.setHttpOnly(false); // 프론트에서 읽어야 하므로 false
        profileCookie.setPath("/");
        profileCookie.setSecure(true); // HTTPS 환경에서는 true

        response.addCookie(cookie);
        response.addCookie(profileCookie);

        response.sendRedirect("http://localhost:3000/oauth2/success");*/


        // json으로 응답 프론트에서 토큰을 꺼내어서 localStorage에 저장
        /*response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"token\": \"" + token + "\", \"isProfileComplete\": " + isProfileComplete + "}");
        response.getWriter().flush();*/

        log.info("✅ [구글 로그인] JWT 발급 완료 - 토큰 값: {}", token);
    }
}



