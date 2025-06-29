FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY third-party-service/target/third-party-service-v1-*.jar /app/third-party-service-v1.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/third-party-service-v1.jar" ]