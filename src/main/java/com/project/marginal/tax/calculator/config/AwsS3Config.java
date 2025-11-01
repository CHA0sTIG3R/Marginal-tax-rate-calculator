package com.project.marginal.tax.calculator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {

    @Value("${tax.s3-region:#{null}}")
    private String s3Region;

    @Bean
    public S3Client s3Client() {
        String region = s3Region;
        if (region == null || region.isBlank()) {
            region = System.getenv().getOrDefault("AWS_REGION", "us-east-1");
        }
        return S3Client.builder()
                .region(Region.of(region))
                .build();
    }
}
