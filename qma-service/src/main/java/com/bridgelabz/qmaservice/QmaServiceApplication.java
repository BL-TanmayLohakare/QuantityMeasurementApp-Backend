package com.bridgelabz.qmaservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ComponentScan(basePackages = "com.bridgelabz.qmaservice")
@EntityScan(basePackages = "com.bridgelabz.qmaservice.model")
@EnableJpaRepositories(basePackages = "com.bridgelabz.qmaservice.repository")
public class QmaServiceApplication {
    private static final Logger logger = LogManager.getLogger(QmaServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(QmaServiceApplication.class, args);
    }
}
