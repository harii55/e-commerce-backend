package org.example.ecommercebackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuration class to enable async processing.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    // Async support is enabled via @EnableAsync annotation
}

