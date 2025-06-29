package com.middleware.customer_service.service;

import com.middleware.customer_service.dto.CustomerOnboardingRequest;
import com.middleware.customer_service.dto.MiddleWareResponse;
import com.middleware.customer_service.dto.ValidationRequest;
import java.util.UUID;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

public interface CustomerService {
    MiddleWareResponse onboardCustomer(CustomerOnboardingRequest request);
    Boolean validateBvn(ValidationRequest request);
    MiddleWareResponse getCustomer(UUID customerId);
    MiddleWareResponse validateCustomerIdentity(ValidationRequest request);
}
