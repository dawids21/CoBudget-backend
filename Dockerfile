FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S stasiakxyz && adduser -S stasiakxyz -G stasiakxyz
USER stasiakxyz:stasiakxyz
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]