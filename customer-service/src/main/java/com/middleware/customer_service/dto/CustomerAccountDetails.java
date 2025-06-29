package com.middleware.customer_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.middleware.common.model.user.CustomerStatus;
import com.middleware.common.model.account.AccountStatus;
import com.middleware.common.model.account.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.UUID;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerAccountDetails {
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
    private BigDecimal balance;
    private AccountType accountType;
    private AccountStatus accountStatus;
}
