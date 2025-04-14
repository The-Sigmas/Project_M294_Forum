# Use a multi-stage build to build the application and create a smaller final image
# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Package the application and run tests
RUN mvn clean verify

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Add wait-for-it script to wait for MongoDB
ADD https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Use wait-for-it to wait for MongoDB before starting the application
ENTRYPOINT ["/wait-for-it.sh", "mongodb:27017", "--timeout=30", "--strict", "--", "java", "-jar", "app.jar"]