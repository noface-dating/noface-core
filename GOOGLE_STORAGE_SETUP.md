# Google Cloud Storage 연동 가이드

## 1. Google Cloud 프로젝트 설정

### 1.1 프로젝트 생성 및 Storage 활성화
1. [Google Cloud Console](https://console.cloud.google.com/) 접속
2. 새 프로젝트 생성 또는 기존 프로젝트 선택
3. **Cloud Storage API** 활성화

### 1.2 Storage Bucket 생성
1. Cloud Storage > Buckets 메뉴로 이동
2. **CREATE BUCKET** 클릭
3. Bucket 이름: `duri-bucket` (또는 원하는 이름)
4. Location type: **Region** 선택
5. Location: **asia-northeast3 (Seoul)** 권장
6. **CREATE** 클릭

### 1.3 서비스 계정 생성 및 권한 부여
1. **IAM & Admin > Service Accounts** 메뉴로 이동
2. **CREATE SERVICE ACCOUNT** 클릭
3. 서비스 계정 이름: `duri-storage-service`
4. 역할(Role) 추가:
   - **Storage Object Admin** (이미지 업로드/삭제용)
   - 또는 **Storage Admin** (전체 관리 권한)
5. **CREATE KEY** 클릭
6. Key type: **JSON** 선택
7. **CREATE** 클릭 → JSON 키 파일 다운로드

## 2. 로컬 환경 설정

### 2.1 서비스 계정 키 파일 배치
다운로드한 JSON 키 파일을 다음 위치에 배치:
```
noface-core/src/main/resources/gcs-key.json
```

**주의**: `.gitignore`에 추가하여 Git에 커밋하지 않도록 주의!

### 2.2 환경 변수 설정 (선택사항)
서비스 계정 키 파일 대신 환경 변수로 설정할 수도 있습니다:

**macOS/Linux:**
```bash
export GOOGLE_APPLICATION_CREDENTIALS="/path/to/gcs-key.json"
```

**Windows:**
```cmd
set GOOGLE_APPLICATION_CREDENTIALS=C:\path\to\gcs-key.json
```

### 2.3 application.properties 설정
```properties
google.cloud.storage.bucket-name=duri-bucket
google.cloud.storage.project-id=your-project-id
```

## 3. 이미지 업로드 방법

### 방법 1: Google Cloud Console에서 직접 업로드 (권장)
1. [Google Cloud Console](https://console.cloud.google.com/storage/browser) 접속
2. `duri-bucket` 선택
3. **UPLOAD FILES** 클릭
4. 폴더 구조에 맞게 업로드:
   ```
   duri-bucket/
   └── face-preference/
       └── female/
           ├── q1/
           │   ├── c1-1.jpg
           │   └── c1-2.jpg
           ├── q2/
           │   ├── c2-1.jpg
           │   └── c2-2.jpg
           ...
   ```

### 방법 2: API를 통한 업로드
애플리케이션 실행 후:

**Postman 또는 curl 사용:**
```bash
curl -X POST http://localhost:8080/api/storage/upload/face-preference \
  -F "file=@/path/to/image.jpg" \
  -F "gender=FEMALE" \
  -F "questionId=q1" \
  -F "choiceId=c1-1"
```

**응답 예시:**
```json
{
  "imageUrl": "https://storage.googleapis.com/duri-bucket/face-preference/female/q1/c1-1.jpg",
  "folderPath": "face-preference/female/q1",
  "fileName": "c1-1.jpg"
}
```

### 방법 3: gsutil 명령어 사용
```bash
# Google Cloud SDK 설치 필요
gsutil cp /path/to/image.jpg gs://duri-bucket/face-preference/female/q1/c1-1.jpg
```

## 4. 이미지 공개 설정

업로드한 이미지를 공개적으로 접근 가능하게 설정:

### 방법 1: Google Cloud Console에서
1. Bucket 선택
2. **PERMISSIONS** 탭 클릭
3. **ADD PRINCIPAL** 클릭
4. Principal: `allUsers`
5. Role: **Storage Object Viewer**
6. **SAVE** 클릭

### 방법 2: gsutil 명령어
```bash
gsutil iam ch allUsers:objectViewer gs://duri-bucket
```

또는 특정 폴더만 공개:
```bash
gsutil -m acl ch -r -u AllUsers:R gs://duri-bucket/face-preference
```

## 5. questions.json 업데이트

이미지 업로드 후, `questions.json`의 `imageUrl`이 실제 업로드된 이미지 URL과 일치하는지 확인:

```json
{
  "gender": "FEMALE",
  "questionId": "q1",
  "choices": [
    {
      "choiceId": "c1-1",
      "imageUrl": "https://storage.googleapis.com/duri-bucket/face-preference/female/q1/c1-1.jpg"
    }
  ]
}
```

## 6. 이미지 URL 확인

업로드된 이미지의 URL을 확인하려면:

```bash
GET http://localhost:8080/api/storage/url/face-preference?gender=FEMALE&questionId=q1&choiceId=c1-1
```

**응답:**
```json
{
  "imageUrl": "https://storage.googleapis.com/duri-bucket/face-preference/female/q1/c1-1.jpg",
  "folderPath": "face-preference/female/q1",
  "fileName": "c1-1.jpg"
}
```

## 7. 문제 해결

### 의존성 오류
```bash
cd noface-core
./gradlew build
```

### 인증 오류
- 서비스 계정 키 파일 경로 확인
- 환경 변수 `GOOGLE_APPLICATION_CREDENTIALS` 확인
- 서비스 계정에 Storage 권한이 있는지 확인

### 이미지 접근 불가
- Bucket 공개 설정 확인
- 이미지 URL이 올바른지 확인
- CORS 설정 확인 (필요한 경우)

## 8. 보안 주의사항

⚠️ **운영 환경에서는:**
- `ImageUploadController`에 인증 추가
- 서비스 계정 키 파일을 환경 변수로 관리
- `.gitignore`에 `gcs-key.json` 추가 확인


