package org.tutortalk.be.domain.welfare.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.tutortalk.be.domain.welfare.dto.ServiceListDto;
import org.tutortalk.be.domain.welfare.dto.WelfareListDto;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Component
public class WelfareApiClient {
    private final WebClient welfareWebClient;
    private final XmlMapper xmlMapper = new XmlMapper();

    private static final Logger log = LoggerFactory.getLogger(WelfareApiClient.class);

    @Value("${external.welfare-api.key}")
    private String serviceKey;

    public List<ServiceListDto> fetchByAge(int age) {
//        System.out.println("[WelfareApiClient] 주입된 서비스 키: " + serviceKey);

        String uri = String.format(
                "https://apis.data.go.kr/B554287/NationalWelfareInformationsV001/NationalWelfarelistV001?serviceKey=%s&callTp=L&pageNo=1&numOfRows=100&srchKeyCode=003&age=%d",
                serviceKey, age
        );

        return welfareWebClient.get()
                .uri(URI.create(uri)) // 전체 URI를 넘김
                .retrieve()
                .bodyToMono(String.class)
                .map(this::deserialize)
                .defaultIfEmpty(List.of())
                .block();
    }

    private List<ServiceListDto> deserialize(String xml) {
        try {
            WelfareListDto dto = xmlMapper.readValue(xml, WelfareListDto.class);
            log.info("[deserialize] xml: {}", xml.replaceAll("\n", ""));

            if (dto.getServList() == null) {
                System.err.println("[deserialize] servList가 null입니다.");
                return List.of();
            }
            return dto.getServList();
        } catch (IOException e) {
            System.err.println("[deserialize] XML 파싱 실패: " + e.getMessage());
            System.err.println("응답 원문:\n" + xml);
            throw new IllegalStateException("XML 파싱 실패", e);
        }
    }
}
