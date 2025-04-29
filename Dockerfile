FROM eclipse-temurin:23.0.2_7-jdk-ubi9-minimal AS builder
WORKDIR /app

# Install xargs
RUN microdnf install -y findutils

COPY gradle/wrapper/ gradle/wrapper/
COPY gradlew gradlew.bat build.gradle.kts gradle.properties settings.gradle.kts ./ 

RUN chmod +x gradlew \
    && ./gradlew --no-daemon dependencies

COPY . .

RUN ./gradlew --no-daemon clean assemble


FROM eclipse-temurin:23.0.2_7-jre-ubi9-minimal
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
