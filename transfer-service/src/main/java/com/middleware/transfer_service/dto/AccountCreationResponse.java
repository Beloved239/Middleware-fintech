package com.middleware.transfer_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountCreationResponse {
    private String accountNumber;
}