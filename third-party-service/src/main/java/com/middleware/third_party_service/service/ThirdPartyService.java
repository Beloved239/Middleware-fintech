package com.middleware.third_party_service.service;

import com.middleware.third_party_service.dto.*;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */
public interface ThirdPartyService {
    KycServiceResponse getBvnDetailsForKYC(KycServiceRequest kycServiceRequest);
    KycServiceResponse getNinDetailsForKYC(KycServiceRequest kycServiceRequest);
    BankResponse getBankList();
    BillersResponse getBiller();
    BillerResponse getBillerProductsDetails(String billerId);
}
