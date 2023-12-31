# Build stage
FROM maven:3.8.3-openjdk-17 AS build
COPY src src
COPY pom.xml .
RUN mvn clean package -DskipTests

# Package stage
FROM openjdk:17-ea-28-jdk-slim
COPY --from=build /target/cloud-storage-0.0.1-SNAPSHOT.jar cloud-storage.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "cloud-storage.jar"]

#RUN ls /target
