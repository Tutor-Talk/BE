package org.tutortalk.be.domain.contents.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tutortalk.be.domain.contents.entity.Content;
import org.tutortalk.be.domain.contents.service.ContentService;

import java.util.Map;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentController {
    private final ContentService contentService;

    @PostMapping("/request")
    public ResponseEntity<?> getContentUrl(@RequestBody Map<String, String> request) {
        String key = request.get("key");
        return contentService.findContentByKey(key)
                .map(content -> {
                    String url = content.getUrl();
                    if (contentService.isUrlValidInS3(url)) {
                        return ResponseEntity.ok(Map.of("url", url));
                    } else {
                        return ResponseEntity.ok(Map.of("url", "유효하지 않은 url"));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
