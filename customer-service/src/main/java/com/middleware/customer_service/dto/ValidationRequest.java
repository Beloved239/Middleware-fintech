package com.middleware.customer_service.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

/**
 * DTO for BVN/NIN validation request
 * Used for identity verification processes
 *
 * @author Oluwatobi Adebanjo
 */
@Data
public class ValidationRequest {
    private UUID customerId;
    private String bvn;
    private String nin;
    private String verificationType;
}
