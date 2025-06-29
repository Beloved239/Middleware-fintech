package com.middleware.customer_service.service;

import com.middleware.customer_service.dto.ValidationRequest;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

public interface ValidationService {
    boolean validateBvn(String bvn, ValidationRequest request);
}
