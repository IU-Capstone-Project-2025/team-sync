FROM gradle:8.5-jdk21 AS build
WORKDIR /app

COPY build.gradle* settings.gradle* gradle/ ./
RUN gradle --no-daemon build || return 0

COPY . .

RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]