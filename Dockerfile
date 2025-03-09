# Use an official OpenJDK runtime as a parent image
FROM openjdk:23-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged JAR file into the container at /app
COPY target/*.jar app.jar

# Specify the command to run on container start
CMD ["java", "-jar", "app.jar"]