FROM eclipse-temurin:21-jre
LABEL authors="kyw10987"

# 컨테이너 내부 작업 디렉토리
WORKDIR /app

# CI에서 전달받은 JAR 복사
COPY build/libs/*.jar app.jar

# 포트 (문서용, 필수 아님)
EXPOSE 8080

# 실행
ENTRYPOINT ["java",
 "-XX:+UseContainerSupport",
 "-XX:MaxRAMPercentage=75.0",
 "-jar",
 "app.jar"]