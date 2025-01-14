package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.example")
@EntityScan("org.example.Database.Objects")
@EnableJpaRepositories("org.example.Database.Repositories")

public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
