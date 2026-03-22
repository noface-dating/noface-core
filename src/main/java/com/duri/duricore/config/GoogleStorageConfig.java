package com.duri.duricore.config;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Google Cloud Storage 설정
 * 
 * 비활성화: application.properties에 google.cloud.storage.enabled=false (기본)
 * 활성화: google.cloud.storage.enabled=true 및 project-id 설정
 * 
 * 인증 방법:
 * 1. 환경 변수: GOOGLE_APPLICATION_CREDENTIALS에 서비스 계정 키 파일 경로 설정
 * 2. 서비스 계정 키 파일을 resources/gcs-key.json에 배치
 * 3. GCP에서 서비스 계정 생성 후 JSON 키 다운로드
 */
@Configuration
@ConditionalOnProperty(name = "google.cloud.storage.enabled", havingValue = "true")
public class GoogleStorageConfig {

    @Value("${google.cloud.storage.project-id}")
    private String projectId;

    @Bean
    public Storage storage() throws IOException {
        // 환경 변수 GOOGLE_APPLICATION_CREDENTIALS가 설정되어 있으면 자동으로 사용
        // 없으면 기본 인증 정보를 사용 (GCP 환경에서 실행 시)
        return StorageOptions.newBuilder()
                .setProjectId(projectId)
                .build()
                .getService();
    }
}


