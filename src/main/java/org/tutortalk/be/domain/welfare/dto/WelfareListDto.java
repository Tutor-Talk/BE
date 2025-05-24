package org.tutortalk.be.domain.welfare.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "wantedList")
@JsonIgnoreProperties(ignoreUnknown = true) // 정의되지 않은 필드가 있어도 무시
public class WelfareListDto {
    private int totalCount;         // 전체 데이터 수
    private int pageNo;             // 페이지 번호
    private int numOfRows;          // 한 페이지 결과 수
    private String resultCode;      // 결과 코드
    private String resultMessage;   // 결과 메시지

    @JacksonXmlProperty(localName = "servList")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<ServiceListDto> servList;
}

