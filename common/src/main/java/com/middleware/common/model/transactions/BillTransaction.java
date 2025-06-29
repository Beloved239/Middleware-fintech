package com.middleware.common.model.transactions;

import com.middleware.common.model.BaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "bills_transactions")
public class BillTransaction extends BaseModel {
    @Column(name = "transaction_ref", nullable = false)
    private String transactionRef;
    @Column(name = "narration", nullable = false)
    private String narration;
    @Column(name = "account_number", nullable = false)
    private String accountNumber;
    @Column(name = "biller_id", nullable = false)
    private String billerId;
    @Column(name = "product_id", nullable = false)
    private String productId;
    @Column(name = "category_id", nullable = false)
    private String categoryId;
    @Column(name = "drCr", nullable = false)
    private String drCr;
    @Column(name = "transaction_amount",  nullable = false, precision = 15, scale = 2)
    private BigDecimal transactionAmount;
}
