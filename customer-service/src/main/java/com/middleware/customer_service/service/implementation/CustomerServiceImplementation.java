package com.middleware.customer_service.service.implementation;


import com.middleware.common.exception.CustomerServiceException;
import com.middleware.common.model.user.CustomerModel;
import com.middleware.common.model.user.CustomerStatus;
import com.middleware.customer_service.Utils;
import com.middleware.customer_service.dto.*;
import com.middleware.common.model.account.Account;
import com.middleware.customer_service.repository.AccountRepository;
import com.middleware.common.repository.CustomerRepository;
import com.middleware.customer_service.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.middleware.common.model.user.CustomerStatus.*;
import static com.middleware.customer_service.Utils.FAILURE_CODE;

/**
 * Customer Service implementation
 * Handles all customer-related business logic
 *
 * @author Oluwatobi Adebanjo
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImplementation implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationService validationService;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final BVNValidationService bvnValidationService;
    private final NINValidationService ninValidationService;
    private final AccountGenerationService accountGenerationService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Onboards a new customer
     *
     * @param request Customer onboarding request
     * @return CustomerResponse
     */
    @Override
    @Transactional
    public MiddleWareResponse onboardCustomer(CustomerOnboardingRequest request) {
        try {
            // Validate input
            if (!isValidRequest(request)) {
                return new MiddleWareResponse(Utils.INVALID_PARAMETERS_CODE, "Invalid input data", false);
            }
            log.info("Starting customer onboarding for email: {}", request.getEmail());

            // Check for existing customer
            if (customerAlreadyExists(request)) {
                return new MiddleWareResponse("46", "Customer already exists with provided details", false);
            }

            // Create customer
            CustomerModel customerModel = createCustomer(request);
            log.info("About to save");
            log.info("Request >>>{}", customerModel);
            CustomerModel savedCustomerModel = customerRepository.save(customerModel);
            log.info("Saved customer model {}", savedCustomerModel);

             // Create account for successfully validated customer
                Account account = accountGenerationService.createAccount(savedCustomerModel);
                account = accountRepository.save(account);
                CustomerAccountDetails details = new CustomerAccountDetails();
                details.setAccountNumber(account.getAccountNumber());
                details.setCustomerId(savedCustomerModel.getId());
                details.setStatus(savedCustomerModel.getStatus());

                return new MiddleWareResponse(
                        "00",
                        "Customer onboarded successfully",true, details
                );
        }  catch (Exception e) {
            log.info("Exception occurred while onboarding customer::{}", e.getMessage());
        return new MiddleWareResponse(FAILURE_CODE,"Onboarding failed: ", false);
    }
    }

    /**
     * Validates customer BVN
     *
     * @param request Validation request
     * @return Boolean indicating validation success
     */
    @Override
    @Transactional
    public Boolean validateBvn(ValidationRequest request) {
        log.info("Validating BVN for customer: {}", request.getCustomerId());

        CustomerModel customerModel = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (!customerModel.getBvn().equals(request.getBvn())) {
            throw new RuntimeException("BVN mismatch");
        }

        // Simulate external BVN validation
        boolean isValid = validationService.validateBvn(request.getBvn(), request);

        return isValid;
    }

    @Override
    public MiddleWareResponse getCustomer(UUID customerId) {
        CustomerModel customerModel = customerRepository.findById(customerId).orElseThrow(()-> new CustomerServiceException("Customer not found"));
        Account account = accountRepository.findFirstByCustomerId(customerId);
        log.info("User's Account Info>>{}", account);
        log.info("User's Mode Info>>{}", customerModel);

        return new MiddleWareResponse("00","Success",true, mapToAccountDetails(customerModel, account));
    }

    @Override
    public MiddleWareResponse validateCustomerIdentity(ValidationRequest request) {
        log.info("Incomming Request>>{}", request);
        try {
            Optional<CustomerModel> optCustomer = customerRepository.findById(request.getCustomerId());

            if (optCustomer.isEmpty()) {
                return new MiddleWareResponse("99", "Customer not found", false);
            }

            CustomerModel customer = optCustomer.get();
            CustomerOnboardingRequest custRequest = new CustomerOnboardingRequest();
            custRequest.setFirstName(customer.getFirstName());
            custRequest.setLastName(customer.getLastName());
            custRequest.setNin(request.getNin());
            custRequest.setBvn(request.getBvn());

            boolean isValidated = performInitialValidation(customer,custRequest, request.getVerificationType());
            if (!isValidated){
                return new MiddleWareResponse("99", "Invalid validation request", false);
            }else {
                return new MiddleWareResponse("00", "Customer Identity Successfully Validated", true);
            }



        } catch (Exception e) {
            log.info("Exception occurred while validating customer identity: {}", e.getMessage());
            return new MiddleWareResponse("99", "Validation failed: ", false);
        }
    }

    private boolean isValidRequest(CustomerOnboardingRequest request) {
        return request.getFirstName() != null && !request.getFirstName().trim().isEmpty() &&
                request.getLastName() != null && !request.getLastName().trim().isEmpty() &&
                request.getEmail() != null && request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$") &&
                request.getPhoneNumber() != null && !request.getPhoneNumber().trim().isEmpty() &&
                (request.getBvn() != null || request.getNin() != null);
    }
    private boolean customerAlreadyExists(CustomerOnboardingRequest request) {
        return customerRepository.existsByEmail(request.getEmail()) ||
                customerRepository.existsByPhoneNumber(request.getPhoneNumber()) ||
                (request.getBvn() != null && customerRepository.existsByBvn(request.getBvn())) ||
                (request.getNin() != null && customerRepository.existsByNin(request.getNin()));
    }

    private CustomerModel createCustomer(CustomerOnboardingRequest request) {
        String bvn = "";
        String nin = "";

        if (request.getBvn() != null && !request.getBvn().trim().isEmpty()) {
            bvn = request.getBvn();
        }

        if (request.getNin() != null && !request.getNin().trim().isEmpty()) {
            nin= request.getNin();
        }
        CustomerModel customerModel = CustomerModel.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .bvn(bvn)
                .nin(nin)
                .address(request.getAddress())
                .status(CustomerStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        log.info("DB Object :::{}", customerModel);


        if (request.getBvn() != null && !request.getBvn().trim().isEmpty()) {
            customerModel.setBvn(request.getBvn());
        }

        if (request.getNin() != null && !request.getNin().trim().isEmpty()) {
            customerModel.setNin(request.getNin());
        }

        return customerModel;
    }

    private boolean performInitialValidation(CustomerModel customerModel, CustomerOnboardingRequest request, String verificationType) {
        if ("BOTH".equalsIgnoreCase(verificationType)) {
            log.info("BOTH");
            return performConcurrentValidation(customerModel, request);
        }
        log.info("NOT BOTH");
        return performSequentialValidation(customerModel, request);
    }

    private boolean performConcurrentValidation(CustomerModel customerModel, CustomerOnboardingRequest request) {
        try {
            CompletableFuture<ValidationResult> bvnTask = createBvnValidationTask(request);
            CompletableFuture<ValidationResult> ninTask = createNinValidationTask(request);

            CompletableFuture.allOf(bvnTask, ninTask).get(30, TimeUnit.SECONDS);

            boolean bvnValid = bvnTask.get().isValid();
            boolean ninValid = ninTask.get().isValid();

            updateCustomerStatus(customerModel, bvnValid, ninValid);
            return finalizeValidation(customerModel);

        } catch (TimeoutException e) {
            log.error("Validation timeout for customer: {}", customerModel.getId(), e);
            return false;
        } catch (Exception e) {
            log.error("Error during concurrent validation for customer: {}", customerModel.getId(), e);
            return false;
        }
    }

    private boolean performSequentialValidation(CustomerModel customerModel, CustomerOnboardingRequest request) {
        boolean bvnValid = validateBvnIfPresent(customerModel, request);
        boolean ninValid = validateNinIfPresent(customerModel, request, bvnValid);

        updateCustomerStatus(customerModel, bvnValid, ninValid);
        return finalizeValidation(customerModel);
    }

    private CompletableFuture<ValidationResult> createBvnValidationTask(CustomerOnboardingRequest request) {
        return CompletableFuture.supplyAsync(() ->
                        bvnValidationService.validateBVN(request.getBvn(), request.getFirstName(), request.getLastName())
                );
    }

    private CompletableFuture<ValidationResult> createNinValidationTask(CustomerOnboardingRequest request) {
        return CompletableFuture.supplyAsync(() ->
                        ninValidationService.validateNIN(request.getNin(), request.getFirstName(), request.getLastName()));
    }

    private boolean validateBvnIfPresent(CustomerModel customerModel, CustomerOnboardingRequest request) {
        if (request.getBvn() == null) return true;

        ValidationResult result = bvnValidationService.validateBVN(
                request.getBvn(), request.getFirstName(), request.getLastName());
        return result.isValid();
    }

    private boolean validateNinIfPresent(CustomerModel customerModel, CustomerOnboardingRequest request, boolean bvnValid) {
        if (request.getNin() == null) return true;

        ValidationResult result = ninValidationService.validateNIN(
                request.getNin(), request.getFirstName(), request.getLastName());
        return result.isValid();
    }

    private void updateCustomerStatus(CustomerModel customerModel, boolean bvnValid, boolean ninValid) {
        if (bvnValid && ninValid) {
            customerModel.setStatus(VERIFIED);
        } else if (bvnValid) {
            customerModel.setStatus(BVN_VERIFIED);
        } else if (ninValid) {
            customerModel.setStatus(NIN_VERIFIED);
        }
    }

    private boolean finalizeValidation(CustomerModel customerModel) {
        if (customerModel.getStatus() != CustomerStatus.PENDING) {
            customerModel.setVerifiedAt(LocalDateTime.now());
            return true;
        }
        return false;
    }

    private MiddleWareResponse validateBVNForCustomer(CustomerModel customerModel, String bvn) {
        if (customerModel.getBvn() != null && !customerModel.getBvn().equals(bvn)) {
            return new MiddleWareResponse("99", "BVN mismatch", false);
        }

        ValidationResult result = bvnValidationService.validateBVN(bvn, customerModel.getFirstName(), customerModel.getLastName());

        if (result.isValid()) {
            customerModel.setBvn(bvn);
            updateCustomerStatus(customerModel, "BVN");
            customerRepository.save(customerModel);

            // Create account if fully verified and no account exists
            createAccountIfEligible(customerModel);

            return new MiddleWareResponse("00", "BVN validated successfully", true);
        }

        return new MiddleWareResponse("99", result.getMessage(), false);
    }

    private void updateCustomerStatus(CustomerModel customerModel, String verificationType) {
        if ("BVN".equals(verificationType)) {
            if (customerModel.getStatus() == NIN_VERIFIED) {
                customerModel.setStatus(VERIFIED);
            } else {
                customerModel.setStatus(BVN_VERIFIED);
            }
        } else if ("NIN".equals(verificationType)) {
            if (customerModel.getStatus() == BVN_VERIFIED) {
                customerModel.setStatus(VERIFIED);
            } else {
                customerModel.setStatus(NIN_VERIFIED);
            }
        }

        if (customerModel.getVerifiedAt() == null) {
            customerModel.setVerifiedAt(LocalDateTime.now());
        }
    }

    private void createAccountIfEligible(CustomerModel customerModel) {
        // Create account if customer is verified and doesn't have one
        if ((customerModel.getStatus() == BVN_VERIFIED ||
                customerModel.getStatus() == NIN_VERIFIED ||
                customerModel.getStatus() == VERIFIED)
//                &&
//                !accountRepository.findByCustomerModelId((customerModel.getId()
        ) {

            Account account = accountGenerationService.createAccount(customerModel);
            accountRepository.save(account);
        }
    }
    public static String getIdentifierType(String bvn, String nin) {
        boolean hasBvn = bvn != null && !bvn.trim().isEmpty();
        boolean hasNin = nin != null && !nin.trim().isEmpty();

        if (hasBvn && hasNin) {
            return "BOTH";
        } else if (hasBvn) {
            return "BVN";
        } else if (hasNin) {
            return "NIN";
        } else {
            return "None";
        }
    }


    private CustomerResponse mapToResponse(CustomerModel savedCustomerModel) {
        return CustomerResponse.builder()
                .customerId(savedCustomerModel.getId())
                .firstName(savedCustomerModel.getFirstName())
                .lastName(savedCustomerModel.getLastName())
                .email(savedCustomerModel.getEmail())
                .phoneNumber(savedCustomerModel.getPhoneNumber())
                .address(savedCustomerModel.getAddress())
                .status(savedCustomerModel.getStatus())
                .bvnVerified(savedCustomerModel.getBvnVerified())
                .ninVerified(savedCustomerModel.getNinVerified())
                .build();
    }

    private CustomerAccountDetails mapToAccountDetails(CustomerModel customerModel, Account account) {
        return CustomerAccountDetails.builder()
                .customerId(customerModel.getId())
                .firstName(customerModel.getFirstName())
                .lastName(customerModel.getLastName())
                .email(customerModel.getEmail())
                .phoneNumber(customerModel.getPhoneNumber())
                .address(customerModel.getAddress())
                .status(customerModel.getStatus())
                .bvnVerified(customerModel.getBvnVerified())
                .ninVerified(customerModel.getNinVerified())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .accountStatus(account.getStatus())
                .accountType(account.getAccountType())
                .build();
    }


}
