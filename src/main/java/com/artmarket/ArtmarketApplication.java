package com.artmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class ArtmarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtmarketApplication.class, args);
    }

}
