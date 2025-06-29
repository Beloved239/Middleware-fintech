package com.middleware.gatewayserver;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.server.WebFilter;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Component
public class Config {
    @Bean
    public WebFilter rateLimiterFilter() {
        return new RateLimiter();
    }
}
