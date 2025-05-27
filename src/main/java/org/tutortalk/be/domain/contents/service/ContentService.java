package org.tutortalk.be.domain.contents.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tutortalk.be.domain.contents.entity.Content;
import org.tutortalk.be.domain.contents.repository.ContentRepository;
// 유효 s3 url 검사
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.net.URI;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final ContentRepository contentRepository;
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

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

    public boolean isUrlValidInS3(String url) {
        try {
            URI uri = URI.create(url);
            String path = uri.getPath();
            String key = path.startsWith("/") ? path.substring(1) : path;
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            s3Client.headObject(headRequest);
            return true;
        } catch (S3Exception e) {
            // 404면 없는 파일
            if (e.statusCode() == 404 || e.statusCode() == 403) return false;
            throw e;
        } catch (Exception e) {
            return false;
        }
    }
}
