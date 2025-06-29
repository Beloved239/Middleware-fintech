package com.middleware.common.repository;

import com.middleware.common.model.user.CustomerModel;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface CustomerRepository extends JpaRepository<CustomerModel, UUID> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<CustomerModel> findByEmail(String email);
    Optional<CustomerModel> findByPhoneNumber(String phoneNumber);
    Optional<CustomerModel> findByBvn(String bvn);
    Optional<CustomerModel> findByNin(String nin);
    boolean existsByBvn(String bvn);
    boolean existsByNin(String nin);
    Optional<CustomerModel> findByEmailOrPhoneNumber(String email, String phoneNumber);
}
