package org.tutortalk.be.domain.welfare.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.tutortalk.be.domain.user.entity.User;
import org.tutortalk.be.domain.user.repository.UserRepository;
import org.tutortalk.be.domain.welfare.dto.WelfareResponseDto;
import org.tutortalk.be.domain.welfare.util.AgeCategoryMapper;
import org.tutortalk.be.domain.welfare.config.WelfareApiClient;
import org.tutortalk.be.domain.welfare.dto.ServiceListDto;
import org.tutortalk.be.domain.welfare.entity.Welfare;
import org.tutortalk.be.domain.welfare.repository.WelfareRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WelfareService {
    private final WelfareApiClient client;
    private final AgeCategoryMapper mapper;
    private final UserRepository userRepository;
    private final WelfareRepository welfareRepository;

    private static final Logger log = LoggerFactory.getLogger(WelfareService.class);

    @Transactional
    public List<WelfareResponseDto> getServicesForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다."));

        int age = user.getAge();
        log.info("로그인한 사용자의 나이: {}", age);

        Set<String> categories = mapper.categories(age);
        log.info("[카테고리] 사용자 나이 기반 생애 주기: {}", categories);
        if (categories.isEmpty()) return List.of();

        List<ServiceListDto> apiResults;
        try {
            apiResults = client.fetchByAge(age);
            if (apiResults == null) {
                log.warn("API 응답이 null입니다.");
                return List.of();
            }
        } catch (Exception e) {
            log.error("API 호출 중 오류", e);
            return List.of();
        }

        log.info("[필터 전] 총 {}건의 복지 서비스 응답됨", apiResults.size());

        List<ServiceListDto> filtered = apiResults.stream()
                .filter(dto -> {
                    return matchesCategory(dto.getIntrsThemaArray(), categories)
                            || matchesCategory(dto.getLifeArray(), categories);
                })
                .toList();

        log.info("[필터 후] 사용자 연령 생애주기 기준 필터링된 수: {}", filtered.size());

        Set<String> existingIds = welfareRepository.findAllById(
                filtered.stream().map(ServiceListDto::getServId).toList()
        ).stream().map(Welfare::getServId).collect(Collectors.toSet());

        List<Welfare> newEntities = filtered.stream()
                .filter(dto -> !existingIds.contains(dto.getServId()))
                .map(dto -> Welfare.builder()
                        .servId(dto.getServId())
                        .servNm(dto.getServNm())
                        .servDgst(dto.getServDgst())
                        .jurMnOn(dto.getJurMnofNm() + dto.getJurOrgNm())
                        .sprtCycNm(dto.getSprtCycNm())
                        .srvPvsnNm(dto.getSrvPvsnNm())
                        .rprsCtadr(dto.getRprsCtadr())
                        .lifeArray(dto.getLifeArray())
                        .intrsThemaArray(dto.getIntrsThemaArray())
                        .servDtlLink(dto.getServDtlLink())
                        .updatedAt(LocalDateTime.now())
                        .build())
                .toList();

        log.info("[DB 저장] 새롭게 저장할 항목: {}건", newEntities.size());
        welfareRepository.saveAll(newEntities);

        return filtered.stream()
                .map(dto -> WelfareResponseDto.builder()
                        .servNm(dto.getServNm())
                        .servDgst(dto.getServDgst())
                        .jurMnOn(dto.getJurMnofNm() + dto.getJurOrgNm())
                        .sprtCycNm(dto.getSprtCycNm())
                        .srvPvsnNm(dto.getSrvPvsnNm())
                        .rprsCtadr(dto.getRprsCtadr())
                        .intrsThemaArray(dto.getIntrsThemaArray())
                        .servDtlLink(dto.getServDtlLink())
                        .build())
                .toList();
    }

    /**
     * 카테고리 문자열 (쉼표 구분)과 매핑된 생애주기(Set) 간 교집합 존재 여부 확인
     */
    private boolean matchesCategory(String raw, Set<String> categories) {
        if (raw == null || raw.isBlank()) return false;
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .anyMatch(categories::contains);
    }

}
