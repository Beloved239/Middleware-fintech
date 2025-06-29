package com.middleware.third_party_service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountInfoResponse {
    private String fullName;
    private String accountNumber;
    private Bank bank;
}
