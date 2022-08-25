FROM registry.vaslapp.com/docker/maven:3.8.1-openjdk-17
COPY ./src/main/resources/application.properties /app/application.properties
COPY ./src/main/resources/application.properties /app/messages.properties
WORKDIR /app
COPY ./target/bank-service-*.jar /app/bank-service.jar
EXPOSE 2020
CMD ["java", "-jar", "bank-service.jar", "--spring.config.location=application.properties"]
