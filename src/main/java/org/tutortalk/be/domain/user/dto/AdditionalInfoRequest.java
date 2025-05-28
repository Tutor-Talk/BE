package org.tutortalk.be.domain.user.dto;

import java.time.LocalDate;

public record AdditionalInfoRequest(
        String name,
        LocalDate birthDate,
        String phone,
        String region
) {}
