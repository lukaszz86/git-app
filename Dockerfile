FROM maven:3.8.4-openjdk-17-slim AS build
COPY pom.xml /app/
COPY src /app/src
RUN mvn -f /app/pom.xml clean package

FROM openjdk:17-jdk-slim
COPY --from=build /app/target/git-app-0.0.1-SNAPSHOT.jar /app/git-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/git-app.jar"]