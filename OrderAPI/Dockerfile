# Build stage
FROM eclipse-temurin:22-jdk AS build
WORKDIR /app

# Copy everything
COPY . .

# Make gradlew executable and build
RUN chmod +x gradlew && \
    ./gradlew clean build -x test --no-daemon

# Run stage
FROM eclipse-temurin:22-jre
WORKDIR /app

RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/build/libs/OrderAPI-0.0.1-SNAPSHOT.jar app.jar
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

EXPOSE 8081
ENTRYPOINT ["/wait-for-it.sh", "mysql", "3306", "java", "-jar", "app.jar"]
