package com.middleware.transfer_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Getter
@Setter
@ToString(callSuper = true)
public class TransactionHistory {
    private BigDecimal amount;
    private String creditOrDebit;
    private String narration;
    private LocalDateTime transactionDate;
}
