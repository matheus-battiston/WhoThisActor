FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY target/*.jar /app/app.jar
EXPOSE 8080 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]