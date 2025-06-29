package com.middleware.customer_service.service;
import com.middleware.customer_service.dto.ValidationResult;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

public interface BVNValidationService {
    ValidationResult validateBVN(String bvn, String firstName, String lastName);

}
