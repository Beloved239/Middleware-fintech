package com.middleware.transfer_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Getter
@Setter
@ToString
public class CreateAccountRequest {
    @NotNull(message = "CustomerId is null")
    @NotEmpty(message = "CustomerId is empty")
    private String customerId;
}
