#
# Build stage
#
FROM gradle:7-jdk AS TEMP_BUILD_IMAGE
ENV APP_HOME=/app/
WORKDIR $APP_HOME

COPY build.gradle.kts settings.gradle.kts gradle.properties $APP_HOME
COPY gradle $APP_HOME/gradle
COPY src $APP_HOME/src

RUN gradle clean build
#
# Run stage
#
FROM openjdk:17-jdk-slim-buster
ENV ARTIFACT_NAME=fat.jar
ENV APP_HOME=/app/

WORKDIR $APP_HOME
COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/$ARTIFACT_NAME .

EXPOSE 8080
ENTRYPOINT exec java -jar ${ARTIFACT_NAME}