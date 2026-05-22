# ─── Stage 1: Build ───────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline -q
COPY src ./src
RUN mvn clean package -DskipTests -q

# ─── Stage 2: Runtime ─────────────────────────────────────────
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN groupadd -r liquifi && useradd -r -g liquifi liquifi
RUN mkdir -p /var/log/liquifi && chown liquifi:liquifi /var/log/liquifi

COPY --from=builder /build/target/*.jar app.jar
RUN chown liquifi:liquifi app.jar

USER liquifi

ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
