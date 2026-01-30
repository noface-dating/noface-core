FROM eclipse-temurin:21-jre
LABEL authors="kyw10987"

WORKDIR /app

COPY build/libs/*.jar app.jar

# 실행
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]