package org.tutortalk.be.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.tutortalk.be.domain.user.entity.User;
import org.tutortalk.be.domain.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));
        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getLastName() + user.getFirstName(), // 이름
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
