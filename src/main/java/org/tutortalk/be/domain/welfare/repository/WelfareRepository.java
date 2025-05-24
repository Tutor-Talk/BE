package org.tutortalk.be.domain.welfare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tutortalk.be.domain.welfare.entity.Welfare;

public interface WelfareRepository extends JpaRepository<Welfare, String> {
}
