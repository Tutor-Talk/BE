package org.tutortalk.be.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.tutortalk.be.global.oauth.GoogleOAuth2SuccessHandler;
import org.tutortalk.be.global.util.CustomUserDetailsService;

@Configuration // 설정 클래스
@EnableWebSecurity // Spring Security 활성화
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;

    @Value("${DEV_PUBLIC_DNS}")
    private String devPublicDns;

    @Value("${DEV_PUBLIC_IP}")
    private String devPublicIp;

    @Value("${PROD_PUBLIC_DNS}")
    private String prodPublicDns;

    @Value("${PROD_PUBLIC_IP}")
    private String prodPublicIp;

    /*
     * 사용자 비밀번호를 안전하게 암호화하기 위하여
     * */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /*
     * 사용자를 검증하고 인증 성공 시 Authentication 겍체를 반환
     * */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .cors(cors -> {}) //CorsConfig에서 설정한 CORS 적용
                .csrf(csrf -> csrf.disable()) // JWT는 CSRF 필요 없으므로 꺼주기
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                        .requestMatchers("/", "/oauth2/**", "/api/contents/**","/login/oauth2/code/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable()) // 로그인 폼 비활성화 (나중에 지워줘야 할 코드)
                .userDetailsService(userDetailsService)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(googleOAuth2SuccessHandler)
                )
                .build();

                /*
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .build();

                 */
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(@Value("${REDIRECT_URI}") String redirectUri) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000","http://127.0.0.1:5500", "http://localhost:5500", redirectUri, devPublicDns,devPublicIp, prodPublicDns, prodPublicIp)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*")
                        .allowCredentials(true); // 인증정보 (쿠키, 인증 헤더) 허용
            }
        };
    }
}
