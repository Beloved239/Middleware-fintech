package com.middleware.transfer_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Getter
@Setter
@ToString
public class BankTransactionRequest {
    @NotEmpty(message = "trn ref is empty")
    private String transactionReference;
    @NotEmpty(message = "bank number is empty")
    private String bankNumber;
    @NotEmpty(message = "other account is empty")
    private String otherAccountNumber;
    @Positive(message = "amount is required")
    private BigDecimal amount;
    @NotEmpty(message = "purpose is empty")
    private String purpose;
}
