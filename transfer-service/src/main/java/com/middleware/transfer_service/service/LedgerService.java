package com.middleware.transfer_service.service;


import com.middleware.common.model.account.Account;
import com.middleware.transfer_service.dto.*;
import org.springframework.http.ResponseEntity;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */


public interface LedgerService {
    ResponseEntity<APIResponse<String>> postBillTransaction(BillTransactionRequest request);

    ResponseEntity<APIResponse<String>> postTransTransaction(BankTransactionRequest request);

    ResponseEntity<APIResponse<Account>> accountDetails();

    ResponseEntity<APIResponse<Top5Response>> top5();
}
