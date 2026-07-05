# Use OpenJDK 21 runtime
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

# Set working directory
WORKDIR /app

# Copy the fat JAR (not the *-original.jar)
COPY target/account-service-0.0.1-SNAPSHOT.jar app.jar

# Expose service port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
