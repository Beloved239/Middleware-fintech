package com.middleware.transfer_service.service.impl;

import com.middleware.common.model.account.Account;
import com.middleware.common.model.transactions.BillTransaction;
import com.middleware.common.model.transactions.Transaction;
import com.middleware.common.model.transactions.TransferTransaction;
import com.middleware.common.model.user.CustomerModel;
import com.middleware.common.security.UserDetailsServices;
import com.middleware.transfer_service.dto.*;
import com.middleware.transfer_service.repository.AccountRepository;
import com.middleware.transfer_service.repository.BillTransactionRepository;
import com.middleware.transfer_service.repository.TransactionRepository;
import com.middleware.transfer_service.repository.TransferTransactionRepository;
import com.middleware.transfer_service.service.LedgerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */


@Slf4j
@Service
@RequiredArgsConstructor
class LedgerServiceImpl implements LedgerService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserDetailsServices userDetailsService;
    private final BillTransactionRepository billTransactionRepository;
    private final TransferTransactionRepository transferTransactionRepository;

    @Value("${account.mirror.bill}")
    private String mirrorBillAccount;
    @Value("${account.mirror.tran}")
    private String mirrorTranAccount;


    @Override
    @Transactional
    public ResponseEntity<APIResponse<String>> postBillTransaction(BillTransactionRequest request) {
        CustomerModel user = userDetailsService.getJWTUserDetails();
        Optional<Account> optionalAccount = accountRepository.findByCustomerId(user.getId());
        Optional<Account> optionalMirrorAccount = accountRepository.findByAccountNumber(mirrorBillAccount);

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>("Account not found", "302", null));
        }

        if (optionalMirrorAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>("Account NOT CONFIGURED", "302", null));
        }

        boolean exists = transactionRepository.existsByTransactionRef(request.getTransactionReference());

        if (exists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse<>("A Transaction with this ref exist", "303", null));
        }

        Account account = optionalAccount.get();
        Account mirrorAccount = optionalMirrorAccount.get();

        if(!StringUtils.equalsIgnoreCase(account.getStatus().toString(), "ACTIVE")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse<>("Account is not ACTIVE", "301", null));
        }

        int result = account.getBalance().compareTo(request.getAmount());

        if (result < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse<>("Insufficient Balance", "301", null));
        }

        String transactionType = "BILL";

        String format = "%s %s %.2f IFO %s | %s";

        String narration = String.format(format, transactionType, account.getAccountNumber(), request.getAmount().doubleValue(), mirrorBillAccount, request.getPurpose());

        Transaction creditPost = new Transaction();
        Transaction debitPost = new Transaction();

        creditPost.setTransactionAmount(request.getAmount());
        creditPost.setTransactionRef(request.getTransactionReference());
        creditPost.setTransactionType(transactionType);
        creditPost.setNarration(narration);
        creditPost.setAccountNumber(mirrorBillAccount);
        creditPost.setDrCr("C");

        BeanUtils.copyProperties(creditPost, debitPost);

        debitPost.setAccountNumber(account.getAccountNumber());
        debitPost.setDrCr("D");

        List<Transaction> transactionList = Arrays.asList(creditPost, debitPost);

        List<Account> accountList = Arrays.asList(account, mirrorAccount);

        BigDecimal currentBalance = account.getBalance().subtract(request.getAmount());
        BigDecimal mirrorCurrentBalance = mirrorAccount.getBalance().add(request.getAmount());

        account.setBalance(currentBalance);
        mirrorAccount.setBalance(mirrorCurrentBalance);


        BillTransaction billTransaction = new BillTransaction();
        BillTransaction billTransactionDebit = new BillTransaction();

        BeanUtils.copyProperties(creditPost, billTransaction);

        billTransaction.setBillerId(request.getBillerId());
        billTransaction.setCategoryId(request.getCategoryId());
        billTransaction.setProductId(request.getProductId());
        billTransaction.setAccountNumber(mirrorBillAccount);
        billTransaction.setDrCr("C");
        billTransaction.setNarration("SOLD " + request.getProductId());


        BeanUtils.copyProperties(billTransaction, billTransactionDebit);

        billTransactionDebit.setNarration("PURCHASED " + request.getProductId());
        billTransactionDebit.setAccountNumber(account.getAccountNumber());
        billTransactionDebit.setDrCr("D");

        List<BillTransaction> billTransactions = Arrays.asList(billTransaction, billTransactionDebit);

        accountRepository.saveAll(accountList);
        transactionRepository.saveAll(transactionList);
        billTransactionRepository.saveAll(billTransactions);

        return ResponseEntity.ok(new APIResponse<>("Transaction completed successfully", "000", request.getTransactionReference()));
    }

    /**
     * @param request
     * @return
     */
    @Override
    @Transactional
    public ResponseEntity<APIResponse<String>> postTransTransaction(BankTransactionRequest request) {
        CustomerModel user = userDetailsService.getJWTUserDetails();
        Optional<Account> optionalAccount = accountRepository.findByCustomerId(user.getId());
        Optional<Account> optionalMirrorAccount = accountRepository.findByAccountNumber(mirrorTranAccount);

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>("Account not found", "302", null));
        }

        if (optionalMirrorAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>("Account NOT CONFIGURED", "302", null));
        }

        boolean exists = transactionRepository.existsByTransactionRef(request.getTransactionReference());

        if (exists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse<>("A Transaction with this ref exist", "303", null));
        }

        Account account = optionalAccount.get();
        Account mirrorAccount = optionalMirrorAccount.get();

        if(!StringUtils.equalsIgnoreCase(account.getStatus().toString(), "ACTIVE")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse<>("Account is not ACTIVE", "301", null));
        }

        int result = account.getBalance().compareTo(request.getAmount());

        if (result < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse<>("Insufficient Balance", "301", null));
        }

        String transactionType = "TRANSFER";

        String format = "%s %s %.2f IFO %s | %s";

        String narration = String.format(format, transactionType, account.getAccountNumber(), request.getAmount().doubleValue(), mirrorTranAccount, request.getPurpose());

        Transaction creditPost = new Transaction();
        Transaction debitPost = new Transaction();

        creditPost.setTransactionAmount(request.getAmount());
        creditPost.setTransactionRef(request.getTransactionReference());
        creditPost.setTransactionType(transactionType);
        creditPost.setNarration(narration);
        creditPost.setAccountNumber(mirrorTranAccount);
        creditPost.setDrCr("C");

        BeanUtils.copyProperties(creditPost, debitPost);

        debitPost.setAccountNumber(account.getAccountNumber());
        debitPost.setDrCr("D");

        List<Transaction> transactionList = Arrays.asList(creditPost, debitPost);

        List<Account> accountList = Arrays.asList(account, mirrorAccount);

        BigDecimal currentBalance = account.getBalance().subtract(request.getAmount());
        BigDecimal mirrorCurrentBalance = mirrorAccount.getBalance().add(request.getAmount());

        account.setBalance(currentBalance);
        mirrorAccount.setBalance(mirrorCurrentBalance);


        TransferTransaction transferTransaction = new TransferTransaction();
        TransferTransaction transferTransactionDebit = new TransferTransaction();

        BeanUtils.copyProperties(creditPost, transferTransaction);

        transferTransaction.setBankNumber(request.getBankNumber());
        transferTransaction.setOtherAccountNumber(request.getOtherAccountNumber());
        transferTransaction.setAccountNumber(mirrorTranAccount);
        transferTransaction.setNarration("RECEIVED FROM " + account.getAccountNumber());
        transferTransaction.setDrCr("C");

        BeanUtils.copyProperties(transferTransaction, transferTransactionDebit);

        transferTransactionDebit.setAccountNumber(account.getAccountNumber());
        transferTransactionDebit.setDrCr("D");
        transferTransactionDebit.setNarration("TRANSFER TO " + request.getOtherAccountNumber());


        List<TransferTransaction> transferTransactions = Arrays.asList(transferTransaction, transferTransactionDebit);

        accountRepository.saveAll(accountList);
        transactionRepository.saveAll(transactionList);
        transferTransactionRepository.saveAll(transferTransactions);

        return ResponseEntity.ok(new APIResponse<>("Transaction completed successfully", "000", request.getTransactionReference()));
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<APIResponse<Account>> accountDetails() {
        CustomerModel user = userDetailsService.getJWTUserDetails();
        Optional<Account> optionalAccount = accountRepository.findByCustomerId(user.getId());

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>("Account not found", "302", null));
        }

        Account userAccount = new Account();
        Account account = optionalAccount.get();

        userAccount.setAccountType(account.getAccountType());
        userAccount.setBalance(account.getBalance());
        userAccount.setAccountNumber(account.getAccountNumber());
        userAccount.setStatus(account.getStatus());

        return ResponseEntity.ok(new APIResponse<>("Account found", "000", userAccount));
    }

    /**
     * @return
     */
    @Override
    public ResponseEntity<APIResponse<Top5Response>> top5() {
        CustomerModel user = userDetailsService.getJWTUserDetails();
        Optional<Account> optionalAccount = accountRepository.findByCustomerId(user.getId());

        if (optionalAccount.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new APIResponse<>("Account not found", "302", null));
        }

        Account account = optionalAccount.get();

        List<BillTransaction> billTransactions = billTransactionRepository.findTop5TransactionsNative(account.getAccountNumber());
        List<TransferTransaction> transferTransactions = transferTransactionRepository.findTop5TransactionsNative(account.getAccountNumber());

        Top5Response top5Response = new Top5Response();

        List<TransactionHistory> billHistory = new ArrayList<>();
        List<TransactionHistory> transacHistory = new ArrayList<>();

        billTransactions.forEach(x -> {
            TransactionHistory transactionHistory = new TransactionHistory();

            transactionHistory.setAmount(x.getTransactionAmount());
            transactionHistory.setCreditOrDebit(x.getDrCr());
            transactionHistory.setTransactionDate(x.getCreatedDate());
            transactionHistory.setNarration(x.getNarration());

            billHistory.add(transactionHistory);
        });

        transferTransactions.forEach(x -> {
            TransactionHistory transactionHistory = new TransactionHistory();

            transactionHistory.setAmount(x.getTransactionAmount());
            transactionHistory.setCreditOrDebit(x.getDrCr());
            transactionHistory.setTransactionDate(x.getCreatedDate());
            transactionHistory.setNarration(x.getNarration());

            transacHistory.add(transactionHistory);
        });

        top5Response.setBillsTransactions(billHistory);
        top5Response.setTransferTransactions(transacHistory);

        return ResponseEntity.ok(new APIResponse<>("TXN found", "000", top5Response));
    }
}
