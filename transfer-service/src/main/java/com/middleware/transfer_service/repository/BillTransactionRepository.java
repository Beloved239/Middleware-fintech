package com.middleware.transfer_service.repository;

import com.middleware.common.model.transactions.BillTransaction;
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
public interface BillTransactionRepository extends JpaRepository<BillTransaction, UUID> {
    @Query(value = "SELECT * FROM bills_transactions a WHERE a.account_number = :accountNumber LIMIT 5", nativeQuery = true)
//    @Query("SELECT a FROM BillTransaction a WHERE a.accountNumber = :accountNumber  LIMIT 5")
    List<BillTransaction> findTop5TransactionsNative(@Param("accountNumber") String accountNumber);
    boolean existsByTransactionRef(String TransactionRef);
}
