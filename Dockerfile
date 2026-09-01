FROM mcr.microsoft.com/openjdk/jdk:21-ubuntu

WORKDIR /app

COPY target/*.jar app.jar

# Expose Spring Boot port (configured as 9000 in application.properties)
EXPOSE 9000

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
