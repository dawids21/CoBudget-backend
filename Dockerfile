FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S dawids21 && adduser -S dawids21 -G dawids21
USER stasiakxyz:stasiakxyz
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]