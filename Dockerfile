FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/api-supermercado-0.0.1.jar
COPY ${JAR_FILE} app_api-superm.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_api-superm.jar"]
