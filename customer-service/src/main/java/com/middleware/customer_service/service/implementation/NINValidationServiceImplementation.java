package com.middleware.customer_service.service.implementation;

import com.middleware.customer_service.dto.ValidationResult;
import com.middleware.customer_service.service.NINValidationService;
import org.springframework.stereotype.Service;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Service
public class NINValidationServiceImplementation implements NINValidationService {

    @Override
    public ValidationResult validateNIN(String nin, String firstName, String lastName) {
        // This would typically make an API call to NIMC or similar service

        try {
            // Simulate API call delay
            Thread.sleep(1000);

            // Basic validation
            if (nin == null || !nin.matches("^\\d{11}$")) {
                return new ValidationResult(false, "Invalid NIN format");
            }

            // Simulate successful validation of NIN But not found in DB for demo purposes
            if (nin.startsWith("999") || nin.startsWith("000")) {
                return new ValidationResult(false, "NIN not found in records");
            }

            //Simulate Succesful validation of NIN (In real scenario, compare with NIMC)
            return new ValidationResult(true, "NIN validated successfully");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new ValidationResult(false, "Validation service temporarily unavailable");
        }
    }
}
