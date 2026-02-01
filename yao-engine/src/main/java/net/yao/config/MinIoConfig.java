package net.yao.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "minio")//去配置文件里找所有以 minio 开头的配置，自动填到这个类的字段里”。
public class MinIoConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    /**
     *
     * @return 客户端
     */
    @Bean
    public MinioClient minioClient(){
        return MinioClient.builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }
}
