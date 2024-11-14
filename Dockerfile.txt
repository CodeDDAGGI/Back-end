FROM amazoncorretto:11-alpine-jdk
ARG JAR_FILE=target/*.jar
ARG PROFILES
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java" , "-Dspring.profiles.active=${PROFILES}", "-jar", "app.jar"]
# "-Dspring.profiles.active=blue" blue로 작성 시 application.yml에 blue를 선택함
# "-Dspring.profiles.active=${PROFILES}" 옵션 추가
# -D : application.yml의 파일 위치