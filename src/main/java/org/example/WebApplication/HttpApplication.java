package org.example.WebApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.example")
@EntityScan("org.example.Database.Objects")
@EnableJpaRepositories("org.example.Database.Repositories")

public class HttpApplication {

	public static void main(String[] args) {
		//logger.info("Order shipped successfully.");
		SpringApplication.run(HttpApplication.class, args);
	}
}
