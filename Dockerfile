FROM eclipse-temurin:23.0.2_7-jre-ubi9-minimal
EXPOSE 8080
RUN mkdir /app
WORKDIR /app/
COPY . .
CMD ["./gradlew", "run"]
