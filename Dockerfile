FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /app

COPY pom.xml .

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B dependency:go-offline

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests && \
    cp target/*.jar /app/app.jar

FROM eclipse-temurin:17-jre

WORKDIR /app

# Runs as the base image's built-in ubuntu user (1000:1000) rather than a
# fresh --system account: 1000:1000 is what the host-owned ./uploads bind
# mount is owned by (see docker-compose.yaml), so a mismatched UID/GID here
# means writes into it fail with AccessDeniedException.
RUN mkdir -p .data/documents && chown -R ubuntu:ubuntu /app

COPY --from=builder /app/app.jar .

USER ubuntu

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+UseG1GC \
               -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --server.address=0.0.0.0"]
