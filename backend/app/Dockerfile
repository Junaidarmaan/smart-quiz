# Step 1: Build stage using Maven and Corretto 21
FROM maven:3.9.6-amazoncorretto-21 AS build

WORKDIR /app

# Copy pom.xml first so dependencies can be cached
COPY pom.xml .

# Download dependencies (safe, does NOT fetch heavy reporting plugins)
RUN mvn -q -DskipTests dependency:resolve

# Copy the entire source code
COPY src ./src

# Build the Spring Boot app
RUN mvn -q -DskipTests clean package


# Step 2: Run stage using lightweight Corretto 21 JDK
FROM amazoncorretto:21-alpine

WORKDIR /app

# Copy jar from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
