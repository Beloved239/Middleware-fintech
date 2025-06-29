package com.middleware.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@SpringBootApplication
@ComponentScan(basePackages = {"com.middleware.common","com.middleware.auth_service"})
@EntityScan(basePackages = {"com.middleware.customer_service.model", "com.middleware.common.model"})
@EnableJpaRepositories(basePackages = {"com.middleware.customer_service.repository","com.middleware.common.repository"})
@EnableDiscoveryClient
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
