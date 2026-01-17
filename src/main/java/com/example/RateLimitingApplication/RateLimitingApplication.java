package com.example.RateLimitingApplication;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RateLimitingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RateLimitingApplication.class, args);
	}

    @Bean
    CommandLineRunner testConnection(ProxyManager<String> proxyManager) {
        return args -> {
            try {
                // Try to resolve a test bucket
                proxyManager.builder().build("test-connection", () -> null);
                System.out.println("✅ Redis Connection Successful!");
            } catch (Exception e) {
                System.err.println("❌ Redis Connection Failed: " + e.getMessage());
            }
        };
    }

}
