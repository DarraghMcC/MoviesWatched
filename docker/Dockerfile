FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
CMD java ${JAVA_OPTS} -jar -Dspring.profiles.active=docker app.jar