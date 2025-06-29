FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY customer-service/target/customer-service-v1-*.jar /app/customer-service-v1.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/customer-service-v1.jar" ]