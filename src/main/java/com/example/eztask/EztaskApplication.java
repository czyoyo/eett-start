package com.example.eztask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EztaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(EztaskApplication.class, args);
    }

}
