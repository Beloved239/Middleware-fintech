FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY auth-service/target/auth-service-v1-*.jar /app/auth-service-v1.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/auth-service-v1.jar" ]