# Этап сборки
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
# Загружаем все зависимости сначала для использования кэша Docker
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Этап запуска
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/phonebook-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]