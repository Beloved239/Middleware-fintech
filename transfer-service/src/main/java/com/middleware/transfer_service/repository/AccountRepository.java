package com.middleware.transfer_service.repository;

import com.middleware.common.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumber(@Param("accountNumber") String accountNumber);
    @Query("SELECT a FROM Account a JOIN a.customer c WHERE c.id = :customerId")
    Optional<Account> findByCustomerId(@Param("customerId") UUID customerId);
    boolean existsByAccountNumber(String accountNumber);
}
