package org.tutortalk.be.domain.contents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutortalk.be.domain.contents.entity.Content;

import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long>{
    Optional<Content> findByClassNameAndNumber(String className, int number);
}
