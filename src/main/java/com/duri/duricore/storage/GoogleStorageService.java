package com.duri.duricore.storage;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * Google Cloud Storage 서비스
 * 
 * 이미지 업로드 및 URL 생성 기능 제공
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnBean(com.google.cloud.storage.Storage.class)
public class GoogleStorageService {

    private final Storage storage;

    @Value("${google.cloud.storage.bucket-name}")
    private String bucketName;

    /**
     * 이미지 파일을 Google Storage에 업로드
     * 
     * @param file 업로드할 파일
     * @param folderPath 저장할 폴더 경로 (예: "face-preference/female/q1")
     * @param fileName 파일명 (예: "c1-1.jpg")
     * @return 업로드된 파일의 공개 URL
     */
    public String uploadImage(MultipartFile file, String folderPath, String fileName) throws IOException {
        String objectName = folderPath + "/" + fileName;
        
        BlobId blobId = BlobId.of(bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        Blob blob = storage.create(blobInfo, file.getBytes());
        
        String publicUrl = "https://storage.googleapis.com/" + bucketName + "/" + objectName;
        log.info("이미지 업로드 완료: {}", publicUrl);
        
        return publicUrl;
    }

    /**
     * 이미지 파일을 Google Storage에 업로드 (자동 파일명 생성)
     * 
     * @param file 업로드할 파일
     * @param folderPath 저장할 폴더 경로
     * @return 업로드된 파일의 공개 URL
     */
    public String uploadImageWithAutoName(MultipartFile file, String folderPath) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName != null && originalFileName.contains(".") 
                ? originalFileName.substring(originalFileName.lastIndexOf(".")) 
                : ".jpg";
        String fileName = UUID.randomUUID().toString() + extension;
        
        return uploadImage(file, folderPath, fileName);
    }

    /**
     * 이미지 URL 생성 (이미 업로드된 파일의 URL 반환)
     * 
     * @param folderPath 폴더 경로
     * @param fileName 파일명
     * @return 공개 URL
     */
    public String getImageUrl(String folderPath, String fileName) {
        return "https://storage.googleapis.com/" + bucketName + "/" + folderPath + "/" + fileName;
    }

    /**
     * 이미지 삭제
     * 
     * @param folderPath 폴더 경로
     * @param fileName 파일명
     * @return 삭제 성공 여부
     */
    public boolean deleteImage(String folderPath, String fileName) {
        String objectName = folderPath + "/" + fileName;
        BlobId blobId = BlobId.of(bucketName, objectName);
        return storage.delete(blobId);
    }

    /**
     * 이미지 존재 여부 확인
     * 
     * @param folderPath 폴더 경로
     * @param fileName 파일명
     * @return 존재 여부
     */
    public boolean imageExists(String folderPath, String fileName) {
        String objectName = folderPath + "/" + fileName;
        BlobId blobId = BlobId.of(bucketName, objectName);
        Blob blob = storage.get(blobId);
        return blob != null && blob.exists();
    }
}


