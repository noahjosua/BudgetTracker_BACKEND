FROM gradle:7.5.1-jdk21 AS build
COPY . .
RUN gradle clean build -x test

FROM openjdk:21.0.3-jdk-slim
COPY --from=build /build/libs/budget-tracker-v1-0.0.1-SNAPSHOT.jar budget-tracker-v1.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "budget-tracker-v1.jar"]