package org.example.ecommercebackend;

import org.example.ecommercebackend.config.DotenvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ECommerceBackendApplication {

    public static void main(String[] args) {
        new DotenvConfig();
        SpringApplication.run(ECommerceBackendApplication.class, args);
    }

}
