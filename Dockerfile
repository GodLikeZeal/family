FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/family-1.0.0.jar app.jar
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","app.jar"]
EXPOSE 8091