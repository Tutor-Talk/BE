package org.tutortalk.be.domain.contents.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tutortalk.be.domain.contents.entity.Content;
import org.tutortalk.be.domain.contents.repository.ContentRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;

    public Optional<Content> findContentByKey(String key) {
        // key 예시: "wifi_3"
        if (key == null || !key.contains("_")) return Optional.empty();

        String[] parts = key.split("_");
        if (parts.length != 2) return Optional.empty();

        String className = parts[0];
        int number;
        try {
            number = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        return contentRepository.findByClassNameAndNumber(className, number);
    }
}
