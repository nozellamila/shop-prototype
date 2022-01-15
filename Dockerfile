FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY /var/jenkins_home/workspace/job-deploy-api/target/shop-prototype.jar app.jar
ENTRYPOINT ["java", "-Xmx512m", "-jar", "/app.jar"]