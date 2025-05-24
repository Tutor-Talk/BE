package org.tutortalk.be.domain.welfare.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WelfareResponseDto {
    private String servNm;          // 서비스 명
    private String servDgst;        // 서비스 요약
    private String jurMnOn;         // 소관 조직명
    private String sprtCycNm;       // 지원 주기
    private String srvPvsnNm;       // 제공 유형
    private String rprsCtadr;       // 문의처
    private String intrsThemaArray; // 관심 주제
    private String servDtlLink;     // 서비스 상세 링크
}
