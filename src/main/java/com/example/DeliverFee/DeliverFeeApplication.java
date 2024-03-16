package com.example.DeliverFee;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class DeliverFeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliverFeeApplication.class, args);
    }
}
