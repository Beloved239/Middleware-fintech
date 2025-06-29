package com.middleware.customer_service.service.implementation;

import com.middleware.customer_service.dto.ValidationRequest;
import com.middleware.customer_service.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationServiceImplementation implements ValidationService {

    @Override
    public boolean validateBvn(String bvn, ValidationRequest request) {
        return false;
    }
}
