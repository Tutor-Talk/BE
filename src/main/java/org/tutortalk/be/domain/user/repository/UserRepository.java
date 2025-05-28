package org.tutortalk.be.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutortalk.be.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}
