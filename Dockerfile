FROM maven:3.9.12-eclipse-temurin-21-noble AS build
WORKDIR /app

COPY settings.xml /root/.m2/settings.xml

COPY pom.xml .
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8002
ENTRYPOINT ["java", "-jar", "app.jar"]