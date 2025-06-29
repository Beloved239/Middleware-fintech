package com.middleware.customer_service.controller;

import com.middleware.customer_service.dto.CustomerOnboardingRequest;
import com.middleware.customer_service.dto.CustomerResponse;
import com.middleware.customer_service.dto.MiddleWareResponse;
import com.middleware.customer_service.dto.ValidationRequest;
import com.middleware.customer_service.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping(value = "/onboard", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MiddleWareResponse> onboardCustomer(@Valid @RequestBody CustomerOnboardingRequest request) {
        //complete customer onboarding process
        MiddleWareResponse response = customerService.onboardCustomer(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/validate-identity")
    public ResponseEntity<MiddleWareResponse> validateBvn(@Valid @RequestBody ValidationRequest request) {
        MiddleWareResponse response = customerService.validateCustomerIdentity(request);
        return ResponseEntity.ok(response);
    }

}
