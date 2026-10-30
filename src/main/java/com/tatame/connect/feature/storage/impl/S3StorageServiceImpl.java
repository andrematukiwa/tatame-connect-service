package com.tatame.connect.feature.storage.impl;

import com.tatame.connect.config.S3Properties;
import com.tatame.connect.feature.storage.StorageService;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class S3StorageServiceImpl implements StorageService {

    private final S3Client s3Client;
    private final S3Properties s3Properties;

    @Override
    public String uploadFile(String path, InputStream content, long contentLength, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
            .bucket(s3Properties.getBucket())
            .key(path)
            .contentType(contentType)
            .build();

        s3Client.putObject(request, RequestBody.fromInputStream(content, contentLength));

        return s3Properties.getPublicBaseUrl() + "/" + path;
    }

    @Override
    public void deleteFile(String path) {
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(s3Properties.getBucket()).key(path).build());
    }
}
