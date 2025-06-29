# Amazon Corretto 17 (JDK) 이미지를 기반으로 사용
FROM amazoncorretto:17-alpine

VOLUME /tmp

# 작업 디렉토리 설정
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
# 애플리케이션 실행 포트 노출
EXPOSE 8080

ENV TZ=Asia/Seoul

# 애플리케이션 실행 명령어
#CMD ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "-Dspring.config.location=file:/app/application-prod.yml", "/app.jar"]
#CMD ["java", "-jar", "-Dspring.config.location=file:/app/application.yml", "/app.jar"]
