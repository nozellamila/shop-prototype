FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ../target/shop-prototype.jar app.jar
ENTRYPOINT ["java", "-Xmx512m", "-jar", "/app.jar"]