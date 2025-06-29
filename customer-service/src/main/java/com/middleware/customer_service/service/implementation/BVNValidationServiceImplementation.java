package com.middleware.customer_service.service.implementation;

import com.middleware.customer_service.dto.ValidationResult;
import com.middleware.customer_service.service.BVNValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Slf4j
@Service
public class BVNValidationServiceImplementation implements BVNValidationService {
    public ValidationResult validateBVN(String bvn, String firstName, String lastName) {
        // This would typically make an API call to NIBSS or similar service
        // For demo purposes, we'll simulate the validation

        try {
            log.info("About to Validate BVN {}", bvn);
            // Simulate API call delay
            Thread.sleep(1000);

            // Basic validation - in real implementation, this would call external API
            if (bvn == null || !bvn.matches("^\\d{11}$")) {
                return new ValidationResult(false, "Invalid BVN format");
            }

            // Simulate successful validation (in real scenario, compare with NIBSS response)
            if (bvn.startsWith("123") || bvn.startsWith("456")) {
                return new ValidationResult(false, "BVN not found in records");
            }

            return new ValidationResult(true, "BVN validated successfully");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ValidationResult(false, "Validation service temporarily unavailable");
        }
    }
}
