# Use an official Gradle image to build the application
FROM gradle:8.7.0-jdk21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and source code
COPY . .

# Build the application
RUN gradle clean build -x test build --no-daemon

# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# Set the entry point to run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]

# docker build -t budget-tracker .
# docker run -p 8080:8080 budget-tracker