FROM maven:3.8.4-openjdk-17 as build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package

FROM openjdk:17

WORKDIR /app

COPY --from=build /app/target/cash_flow_bot-1.0-SNAPSHOT.jar app.jar

EXPOSE 8889

CMD ["java", "-jar", "app.jar"]