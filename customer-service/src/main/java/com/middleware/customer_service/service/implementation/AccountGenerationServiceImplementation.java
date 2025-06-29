package com.middleware.customer_service.service.implementation;

import com.middleware.common.model.user.CustomerModel;
import com.middleware.common.model.account.Account;
import com.middleware.customer_service.repository.AccountRepository;
import com.middleware.customer_service.service.AccountGenerationService;
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
public class AccountGenerationServiceImplementation implements AccountGenerationService {

    private final AccountRepository accountRepository;

    @Override
    public Account createAccount(CustomerModel customerModel) {

        String accountNumber = generateUniqueAccountNumber();
        return new Account(customerModel, accountNumber);
    }

    private String generateUniqueAccountNumber() {
        log.info("Generating unique account number");
        String accountNumber;
        do {
            accountNumber = "50" + String.format("%08d", (int)(Math.random() * 100000000));
        } while (accountRepository.existsByAccountNumber(accountNumber));

        log.info("Generated unique account number: {}", accountNumber);
        return accountNumber;
    }
}
