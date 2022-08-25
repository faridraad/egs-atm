FROM registry.vaslapp.com/docker/maven:3.8.1-openjdk-17
COPY ./src/main/resources/application.properties /app/application.properties
COPY ./src/main/resources/message.properties /app/message.properties
WORKDIR /app
COPY ./target/atm-service-*.jar /app/atm-service.jar
EXPOSE 1010
CMD ["java", "-jar", "atm-service.jar", "--spring.config.location=application.properties"]
