FROM eclipse-temurin:22-jdk AS build
WORKDIR /app

COPY . .

RUN chmod +x gradlew && \
    ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:22-jre
WORKDIR /app

COPY --from=build /app/build/libs/Mehana-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
