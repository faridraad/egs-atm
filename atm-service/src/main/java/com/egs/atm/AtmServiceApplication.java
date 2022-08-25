package com.egs.atm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AtmServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AtmServiceApplication.class, args);
    }
}
