package com.duri.duricore.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 이미지 업로드 테스트용 컨트롤러
 * 
 * 개발/테스트 환경에서만 사용하고, 운영에서는 제거하거나 보안 강화 필요
 */
@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
@ConditionalOnBean(GoogleStorageService.class)
public class ImageUploadController {

    private final GoogleStorageService googleStorageService;

    /**
     * 얼굴 취향 이지선다 이미지 업로드
     * 
     * 예시:
     * POST /api/storage/upload/face-preference
     * Content-Type: multipart/form-data
     * 
     * Form Data:
     * - file: 이미지 파일
     * - gender: FEMALE 또는 MALE
     * - questionId: q1, q2, ..., q10
     * - choiceId: c1-1, c1-2, c2-1, ...
     */
    @PostMapping("/upload/face-preference")
    public ResponseEntity<Map<String, String>> uploadFacePreferenceImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("gender") String gender,
            @RequestParam("questionId") String questionId,
            @RequestParam("choiceId") String choiceId) throws IOException {
        
        // 폴더 경로: face-preference/{gender}/{questionId}
        String folderPath = "face-preference/" + gender.toLowerCase() + "/" + questionId;
        
        // 파일명: {choiceId}.jpg (또는 원본 확장자)
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName != null && originalFileName.contains(".") 
                ? originalFileName.substring(originalFileName.lastIndexOf(".")) 
                : ".jpg";
        String fileName = choiceId + extension;
        
        String imageUrl = googleStorageService.uploadImage(file, folderPath, fileName);
        
        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", imageUrl);
        response.put("folderPath", folderPath);
        response.put("fileName", fileName);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 이미지 URL 생성 (업로드 없이 URL만 생성)
     * 
     * 이미 업로드된 이미지의 URL을 확인할 때 사용
     */
    @GetMapping("/url/face-preference")
    public ResponseEntity<Map<String, String>> getFacePreferenceImageUrl(
            @RequestParam("gender") String gender,
            @RequestParam("questionId") String questionId,
            @RequestParam("choiceId") String choiceId) {
        
        String folderPath = "face-preference/" + gender.toLowerCase() + "/" + questionId;
        String fileName = choiceId + ".jpg";
        String imageUrl = googleStorageService.getImageUrl(folderPath, fileName);
        
        Map<String, String> response = new HashMap<>();
        response.put("imageUrl", imageUrl);
        response.put("folderPath", folderPath);
        response.put("fileName", fileName);
        
        return ResponseEntity.ok(response);
    }
}


