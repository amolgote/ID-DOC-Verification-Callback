FROM openjdk:11.0.3-jdk
VOLUME /tmp
ADD target/*.jar app.jar
EXPOSE 9090
ENTRYPOINT exec java -Djava.awt.headless=true -jar /app.jar