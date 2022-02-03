package com.idfinance.cryptocurrency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CryptocurrencyApplication {

    public static void main(String[] args) {
//        System.setProperty("spring.profiles.active", "test");
        SpringApplication.run(CryptocurrencyApplication.class, args);
    }

}
