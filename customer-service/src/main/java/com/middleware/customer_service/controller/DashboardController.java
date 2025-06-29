package com.middleware.customer_service.controller;

import com.middleware.customer_service.dto.CustomerOnboardingRequest;
import com.middleware.customer_service.dto.CustomerResponse;
import com.middleware.customer_service.dto.MiddleWareResponse;
import com.middleware.customer_service.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final CustomerService customerService;

    @GetMapping("/account-info/{customerId}")
    public ResponseEntity<MiddleWareResponse> getCustomer(@PathVariable UUID customerId) {
        MiddleWareResponse response = customerService.getCustomer(customerId);
        return ResponseEntity.ok(response);
    }
}
