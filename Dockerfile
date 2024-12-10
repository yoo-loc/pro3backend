# Use a base image with OpenJDK 21
FROM eclipse-temurin:21-jdk-jammy AS build

# Set the working directory
WORKDIR /backend

# Copy the Gradle wrapper files first (to leverage Docker caching)
COPY gradlew .
COPY gradle gradle

# Add permissions to execute the Gradle wrapper
RUN chmod +x gradlew

# Copy the remaining project files
COPY . .

# Build the application using Gradle (avoids running in container later)
RUN ./gradlew bootJar

# Use a minimal JRE image for runtime
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the built JAR file from the previous build stage
COPY --from=build /backend/build/libs/*.jar app.jar

# Expose the backend port
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "app.jar"]
