# Step 1: Build stage using Maven and Corretto 21
FROM maven:3.9.6-amazoncorretto-21 AS build

WORKDIR /app

# Copy pom.xml first so dependencies are cached
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire source code
COPY src ./src

# Build the Spring Boot app
RUN mvn -DskipTests clean package


# Step 2: Run stage using lightweight Corretto 21 JDK
FROM amazoncorretto:21-alpine

WORKDIR /app

# Copy jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Render default port handling
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
