package com.middleware.transfer_service.repository;

import com.middleware.common.model.transactions.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query(value = "SELECT * FROM Transaction a WHERE a.accountNumber = :accountNumber LIMIT 5", nativeQuery = true)
    List<Transaction> findTop5TransactionsNative(@Param("accountNumber") String accountNumber);
    boolean existsByTransactionRef(String TransactionRef);
}
