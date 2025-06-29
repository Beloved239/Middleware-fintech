package com.middleware.customer_service.dto;

import com.middleware.common.model.user.CustomerStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

/**
 * DTO for customer response
 * Contains customer information returned to clients
 *
 * @author Oluwatobi Adebanjo
 */
@Data
@Builder
public class CustomerResponse {
    private UUID customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private CustomerStatus status;
    private Boolean bvnVerified;
    private Boolean ninVerified;
    private String accountNumber;
}
