package com.example.steam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SteamApplication {

    public static void main(String[] args) {
        SpringApplication.run(SteamApplication.class, args);
    }

}
