FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/report-microservice-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8081

CMD ["java", "-jar", "report-microservice-0.0.1-SNAPSHOT.jar"]
