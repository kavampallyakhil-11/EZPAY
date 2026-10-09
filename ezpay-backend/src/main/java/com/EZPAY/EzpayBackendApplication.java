package com.EZPAY;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.EZPAY",
        "controller",
        "Service",
        "repository",
        "entity",
        "dto",
        "enums",
        "exception"
})
@EnableJpaRepositories(basePackages = "repository")
@EntityScan(basePackages = "entity")
public class EzpayBackendApplication {

    private static final Logger logger =
            LoggerFactory.getLogger(EzpayBackendApplication.class);

    public static void main(String[] args) {

        logger.info("========================================");
        logger.info("Starting EZPAY Backend Application");
        logger.info("========================================");

        SpringApplication.run(
                EzpayBackendApplication.class,
                args
        );

        logger.info("========================================");
        logger.info("EZPAY Backend Application Started");
        logger.info("Server URL: http://localhost:8083");
        logger.info("Transaction API: http://localhost:8083/api/transactions");
        logger.info("========================================");
    }
}