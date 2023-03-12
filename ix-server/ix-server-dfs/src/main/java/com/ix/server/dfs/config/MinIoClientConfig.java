package com.ix.server.dfs.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Zeng canwei
 * @version 1.8
 * @date 2023/2/28 0028
 */
@Data
@Component
@ConfigurationProperties(prefix = "oss.minio")
public class MinIoClientConfig {
//    @Value("${minio.endpoint}")
    private String endpoint;
//    @Value("${minio.accessKey}")
    private String accessKey;
//    @Value("${minio.secretKey}")
    private String secretKey;
//    @Value("${minio.bucketName}")
    private String bucketName;

    /**
     * 注入minio 客户端
     * @return
     */
    @Bean
    public MinioClient minioClient(){

        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

}
