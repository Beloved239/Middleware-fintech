package com.middleware.customer_service.service;

import com.middleware.common.model.user.CustomerModel;
import com.middleware.common.model.account.Account;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

public interface AccountGenerationService {
    Account createAccount(CustomerModel customerModel);
}
