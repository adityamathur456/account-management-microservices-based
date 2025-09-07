package com.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.transaction.client")
public class TransactionServiceApp {
    public static void main( String[] args ) {
        SpringApplication.run(TransactionServiceApp.class, args);
    }
}
