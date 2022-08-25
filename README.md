# EGS ATM
Simple Atm service and client created by spring boot!

I create a Spring Boot project(Rest APIs). You need the following tools and technologies to develop the same.
- Spring-Boot 2.5.5
- Springfox-swagger2 
- Lombok 1.18.20
- MapStruct 1.4.2.Final
- JavaSE 17
- MongoDB

# Dependencies
Open the pom.xml file for spring configuration:

      <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.5</version>
        <relativePath/> <!-- lookup parent from repository -->
      </parent>

and dpendencies:

       <!-- SPRING -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
         <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
       <!-- PROJECT LOMBOK -->
         <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!-- MAPSTRUCT -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${org.mapstruct.version}</version>
        </dependency>
         


# Features

1. User capable to set own preferred authentication method (example - PIN, fingerprint.)
2. Simplistic credit card validation mechanism when card number is provided ("Block" card after three unsuccessful attempts)
3. After successful authentication User capable to do standard list of operations with ATM: Cash deposit, Cash withdrawal, Check balance
4. Integration and Unit Test
5. Swagger 
6. ATM and Bank services are present stand up microservice architecture
7. Docker file