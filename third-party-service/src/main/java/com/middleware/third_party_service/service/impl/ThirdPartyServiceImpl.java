package com.middleware.third_party_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.middleware.common.converter.JsonConverter;
import com.middleware.common.exception.CustomerServiceException;
import com.middleware.third_party_service.dto.*;
import com.middleware.third_party_service.service.ThirdPartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Slf4j
@Service
@RequiredArgsConstructor
class ThirdPartyServiceImpl implements ThirdPartyService {
    @Override
    public KycServiceResponse getBvnDetailsForKYC(KycServiceRequest kycServiceRequest) {
        KycServiceResponse kycServiceResponse = getKYCBVN(kycServiceRequest.getKycNumber());

        if (kycServiceResponse == null) {
            throw new CustomerServiceException("KYC not found");
        }

        return kycServiceResponse;
    }

    @Override
    public KycServiceResponse getNinDetailsForKYC(KycServiceRequest kycServiceRequest) {
        KycServiceResponse kycServiceResponse = getKYCNIN(kycServiceRequest.getKycNumber());

        if (kycServiceResponse == null) {
            throw new CustomerServiceException("KYC not found");
        }

        return kycServiceResponse;
    }

    @Override
    public BankResponse getBankList() {
        BankResponse bankResponse = returnBanks();

        if (bankResponse == null) {
            throw new CustomerServiceException("Banks not found");
        }

        return bankResponse;
    }



    /**
     * @return
     */
    @Override
    public BillersResponse getBiller() {
        BillersResponse billersResponse = getXBiller();

        if (billersResponse == null) {
            throw new CustomerServiceException("Biller not found");
        }

        return billersResponse;
    }

    /**
     * @param billerId
     * @return
     */
    @Override
    public BillerResponse getBillerProductsDetails(String billerId) {
        BillerResponse billersResponse = getXBillerProducts(billerId);

        if (billersResponse == null) {
            throw new CustomerServiceException("Biller not found");
        }

        return billersResponse;
    }

    public BillersResponse getXBiller() {
        ClassPathResource classPathResource = new ClassPathResource("static/billData.json");
        byte[] binaryData = null;
        try {
            binaryData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BillersResponse billersResponse = new BillersResponse();

        JsonNode jsonNode = JsonConverter.toJsonNode(binaryData);

        if (jsonNode.isArray()) {
            List<BillerResponse> billerResponseList = new ArrayList<>();
            billersResponse.setBillerResponses(billerResponseList);

            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode node = arrayNode.get(i);

                log.debug("NODE: {}", JsonConverter.toJson(node, true));

                String billerId = node.get("billerId").asText();
                String billerName = node.get("billerName").asText();

                BillerResponse billerResponse = new BillerResponse();

                billerResponse.setBillerId(billerId);
                billerResponse.setBillerName(billerName);

                billerResponseList.add(billerResponse);
            }

            return billersResponse;
        }

        return null;
    }


    public BillerResponse getXBillerProducts(String billerId) {
        ClassPathResource classPathResource = new ClassPathResource("static/billData.json");
        byte[] binaryData = null;
        try {
            binaryData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonNode jsonNode = JsonConverter.toJsonNode(binaryData);

        if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode node = arrayNode.get(i);

                String billerXId = node.get("billerId").asText();

                if (StringUtils.equalsIgnoreCase(billerXId, billerId)) {
                    log.debug("SEEN: {}", node);
                    String billerName = node.get("billerName").asText();

                    BillerResponse billerResponse = new BillerResponse();

                    ArrayNode categoryNode = (ArrayNode) node.get("categories");
                    List<Category> categoryList = new ArrayList<>();

                    billerResponse.setBillerName(billerName);
                    billerResponse.setBillerId(billerXId);
                    billerResponse.setCategoryList(categoryList);

                    for (int j = 0; j < categoryNode.size(); j++) {
                        JsonNode catNode = categoryNode.get(j);

                        Category category = new Category();

                        categoryList.add(category);

                        List<Product> productList = new ArrayList<>();

                        category.setProductList(productList);

                        String catName = catNode.get("categoryName").asText();
                        String catId = catNode.get("categoryId").asText();

                        category.setCategoryName(catName);
                        category.setCategoryId(catId);

                        ArrayNode productsNode = (ArrayNode) catNode.get("products");

                        for (int k = 0; k < productsNode.size(); k++) {
                            JsonNode product = productsNode.get(k);

                            Product product1 = new Product();

                            String productId = product.get("productId").asText();
                            String productName = product.get("productName").asText();

                            product1.setProductId(productId);
                            product1.setProductName(productName);

                            productList.add(product1);
                        }
                    }
                    return billerResponse;
                }
            }
        }

        return null;
    }

    private KycServiceResponse getKYCNIN(String kycNumber) {
        ClassPathResource classPathResource = new ClassPathResource("static/identityData.json");
        byte[] binaryData = null;
        try {
            binaryData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonNode jsonNode = JsonConverter.toJsonNode(binaryData);

        if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode node = arrayNode.get(i);

                log.debug("NODE: {}", JsonConverter.toJson(node, true));

                String number = node.get("nin").asText();

                if (StringUtils.equalsIgnoreCase(number, kycNumber)) {
                    KycServiceResponse kycServiceResponse = new KycServiceResponse();

                    kycServiceResponse.setIdentityNumber(number);
                    kycServiceResponse.setDob(node.get("dateOfBirth").asText());
                    kycServiceResponse.setFullName(node.get("fullName").asText());

                    return kycServiceResponse;
                }
            }
        }

        return null;
    }
    private KycServiceResponse getKYCBVN(String kycNumber) {
        ClassPathResource classPathResource = new ClassPathResource("static/identityData.json");
        byte[] binaryData = null;
        try {
            binaryData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonNode jsonNode = JsonConverter.toJsonNode(binaryData);

        if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode node = arrayNode.get(i);

                log.debug("NODE: {}", JsonConverter.toJson(node, true));

                String number = node.get("bvn").asText();

                if (StringUtils.equalsIgnoreCase(number, kycNumber)) {
                    KycServiceResponse kycServiceResponse = new KycServiceResponse();

                    kycServiceResponse.setIdentityNumber(number);
                    kycServiceResponse.setDob(node.get("dateOfBirth").asText());
                    kycServiceResponse.setFullName(node.get("fullName").asText());

                    return kycServiceResponse;
                }
            }
        }

        return null;
    }

    private BankResponse returnBanks() {
        ClassPathResource classPathResource = new ClassPathResource("static/bankData.json");
        byte[] binaryData = null;
        try {
            binaryData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonNode jsonNode = JsonConverter.toJsonNode(binaryData);


        if (jsonNode.isArray()) {
            ArrayList<Bank> banks = new ArrayList<>();
            BankResponse bankResponse = new BankResponse();

            bankResponse.setBanks(banks);

            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode node = arrayNode.get(i);

                log.debug("NODE: {}", JsonConverter.toJson(node, true));

                String name = node.get("bankName").asText();
                String number = node.get("bankNumber").asText();

                Bank bank = new Bank();

                bank.setBankName(name);
                bank.setBankNumber(number);

                banks.add(bank);
            }

            return bankResponse;
        }

        return null;
    }

    private AccountInfoResponse getAccountInfo(String bankNumber, String accountNumber) {
        ClassPathResource classPathResource = new ClassPathResource("static/bankData.json");
        byte[] binaryData = null;
        try {
            binaryData = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonNode jsonNode = JsonConverter.toJsonNode(binaryData);


        if (jsonNode.isArray()) {

            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                JsonNode node = arrayNode.get(i);

                log.debug("NODE: {}", JsonConverter.toJson(node, true));

                String bankNumber1 = node.get("bankNumber").asText();

                if (StringUtils.equalsIgnoreCase(bankNumber1, bankNumber)) {
                    String bankName = node.get("bankName").asText();
                    Bank bank = new Bank();

                    bank.setBankName(bankName);
                    bank.setBankNumber(bankNumber1);

                    ArrayNode accountArrayNode = (ArrayNode) node.get("accounts");

                    for (int j = 0; j < accountArrayNode.size(); j++) {
                        JsonNode accountNode = accountArrayNode.get(j);

                        String customerNumber = accountNode.get("accountNumber").asText();

                        if (StringUtils.equalsIgnoreCase(customerNumber, accountNumber)) {
                            String accountName = accountNode.get("accountName").asText();

                            AccountInfoResponse accountInfoResponse = new AccountInfoResponse();

                            accountInfoResponse.setAccountNumber(accountNumber);
                            accountInfoResponse.setFullName(accountName);
                            accountInfoResponse.setBank(bank);

                            return accountInfoResponse;
                        }
                    }
                }
            }
        }

        return null;
    }
}
