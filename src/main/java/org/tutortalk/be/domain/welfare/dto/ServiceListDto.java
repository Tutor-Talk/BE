package org.tutortalk.be.domain.welfare.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true) // 정의되지 않은 필드가 있어도 무시
@Data
public class ServiceListDto {
    private String inqNum;              // 조회 수
    private String intrsThemaArray;     // 관심 주제
    private String jurMnofNm;           // 소관 부처명 (ex 보건복지부)
    private String jurOrgNm;            // 소관 조직명
    private String lifeArray;           // 생애 주기
    private String onapPsbltYn;         // 온라인 신청 가능 여부
    private String rprsCtadr;           // 문의처 (ex 1522-0365)
    private String servDgst;            // 서비스 요약
    private String servDtlLink;         // 서비스 상세 링크
    private String servId;              // 서비스 ID
    private String servNm;              // 서비스 명
    private String sprtCycNm;           // 지원 주기 (ex 1회성)
    private String srvPvsnNm;           // 제공 유형 (ex 전자바우처)
    private String svcfrstRegTs;        // 서비스 등록일
    private String trgterIndvdlArray;   // 가구 유형
}
