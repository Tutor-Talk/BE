package org.tutortalk.be.domain.welfare.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "welfare")
public class Welfare {

    /** 서비스 ID (예: WLF00000031) */
    @Id
    @Column(name = "serv_id", length = 11)
    private String servId;

    /** 서비스 명 (servNm) */
    @Column(name = "name", length = 100)
    private String servNm;

    /** 요약 설명 (servDgst) */
    @Column(name = "summary", length = 500)
    private String servDgst;

    /** 담당 부처 (jurMnofNm + jurOrgNm) */
    @Column(name = "department", length = 200)
    private String jurMnOn;

    /** 지원 주기 (sprtCycNm) */
    @Column(name = "cycle", length = 500)
    private String sprtCycNm;

    /** 제공 유형 (srvPvsnNm) */
    @Column(name = "type", length = 500)
    private String srvPvsnNm;

    /** 문의처 (rprsCtadr) */
    @Column(name = "contact", length = 500)
    private String rprsCtadr;


    /** 생애 주기 (lifeArray)
     * 영유아, 아동, 청소년, 청년, 중장년, 노년, 임신 · 출산 */
    @Column(name = "life_array", length = 100)
    private String lifeArray;

    /** 관심 주제 (intrsThemaArray) → 콤마 구분 문자열로 저장 */
    @Column(name = "category", length = 100)
    private String intrsThemaArray;

    /** 서비스 상세 링크 (servDtlLink) */
    @Column(name = "detail_link", length = 500)
    private String servDtlLink;

    /** 마지막 동기화 시각 */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Welfare() {}

    @Builder
    private Welfare(String servId, String servNm, String servDgst,
                    String jurMnOn, String sprtCycNm, String srvPvsnNm, String rprsCtadr,
                    String lifeArray, String intrsThemaArray, String servDtlLink, LocalDateTime updatedAt) {
        this.servId = servId;
        this.servNm = servNm;
        this.servDgst = servDgst;
        this.jurMnOn = jurMnOn;
        this.sprtCycNm = sprtCycNm;
        this.srvPvsnNm = srvPvsnNm;
        this.rprsCtadr = rprsCtadr;
        this.lifeArray = lifeArray;
        this.intrsThemaArray = intrsThemaArray;
        this.servDtlLink = servDtlLink;
        this.updatedAt = updatedAt;
    }

//    /** 내용 갱신 */
//    public void updateFromApi(String name, String summary,
//                              String category, String detailLink, LocalDateTime now) {
//        this.name = name;
//        this.summary = summary;
//        this.category = category;
//        this.detailLink = detailLink;
//        this.updatedAt = now;
//    }
}
