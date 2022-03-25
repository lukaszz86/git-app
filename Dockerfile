FROM maven:3.6.0-jdk-11-slim AS build
COPY pom.xml /app/
COPY src /app/src
RUN mvn -f /app/pom.xml clean package

FROM adoptopenjdk/openjdk11:alpine-slim
COPY --from=build /app/target/git-app-0.0.1-SNAPSHOT.jar /app/git-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/git-app.jar"]