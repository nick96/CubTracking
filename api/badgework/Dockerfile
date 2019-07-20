FROM openjdk:8-jdk-alpine AS builder

WORKDIR /build
COPY . /build
RUN ./gradlew bootJar

FROM openjdk:8-jdk-alpine

WORKDIR /app
COPY --from=builder /build/build/libs/badgework.jar /app

CMD java -jar ./badgework.jar


