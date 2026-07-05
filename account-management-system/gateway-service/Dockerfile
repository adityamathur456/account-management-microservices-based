# Use OpenJDK 21 runtime
FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

# Set working directory
WORKDIR /app

# Copy the fat JAR (not the *-original.jar)
COPY target/gateway-service-1.0.0.jar app.jar

# Expose service port
EXPOSE 8085

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
