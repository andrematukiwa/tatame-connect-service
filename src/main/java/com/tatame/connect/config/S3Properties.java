package com.tatame.connect.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "aws.s3")
public class S3Properties {

    private String endpoint;
    private String region = "auto";
    private String accessKey;
    private String secretKey;
    private boolean pathStyleAccess = true;
    private String bucket;

    /** URL base para montar o link publico do arquivo (ex: endpoint do MinIO local, ou dominio publico do bucket no R2). */
    private String publicBaseUrl;
}
