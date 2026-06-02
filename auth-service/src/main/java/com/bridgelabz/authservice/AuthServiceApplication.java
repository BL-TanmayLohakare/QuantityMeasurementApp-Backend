package com.bridgelabz.authservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.bridgelabz.authservice")
@EntityScan(basePackages = "com.bridgelabz.authservice.model")
@EnableJpaRepositories(basePackages = "com.bridgelabz.authservice.repository")
public class AuthServiceApplication {
    private static final Logger logger = LogManager.getLogger(AuthServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
}
