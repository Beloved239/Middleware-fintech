package com.middleware.transfer_service.controller;

import com.middleware.common.model.account.Account;
import com.middleware.transfer_service.dto.APIResponse;
import com.middleware.transfer_service.dto.BankTransactionRequest;
import com.middleware.transfer_service.dto.BillTransactionRequest;
import com.middleware.transfer_service.dto.Top5Response;
import com.middleware.transfer_service.service.LedgerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfer")
public class TransferController {
    private final LedgerService ledgerService;

    @PostMapping(value = "post-bill-transaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<String>> postBillTransaction(@RequestBody @Valid BillTransactionRequest request){
        return ledgerService.postBillTransaction(request);
    }

    @PostMapping(value = "post-trans-transaction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<String>> postTransTransaction(@RequestBody @Valid BankTransactionRequest request){
        return ledgerService.postTransTransaction(request);
    }

    @GetMapping(value = "get-account-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<Account>> accountDetails(){
        return ledgerService.accountDetails();
    }

    @GetMapping(value = "top-five-bill-trans", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<APIResponse<Top5Response>> top5(){
        return ledgerService.top5();
    }
}
