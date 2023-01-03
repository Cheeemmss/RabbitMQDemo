package com.wl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RabbitMQApplication {
    public static void main(String[] args) {
        SpringApplication.run(RabbitMQApplication.class, args);
    }
}
