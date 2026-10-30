FROM maven:3.9.11-eclipse-temurin-17 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B -ntp dependency:go-offline
COPY src ./src
RUN mvn -B -ntp package -DskipTests

FROM eclipse-temurin:17-jre-jammy
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system calculator \
    && useradd --system --gid calculator --home-dir /app calculator
WORKDIR /app
COPY --from=build --chown=calculator:calculator /build/target/calculator.jar app.jar
USER calculator
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
