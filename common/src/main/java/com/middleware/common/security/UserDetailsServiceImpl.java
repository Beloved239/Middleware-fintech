package com.middleware.common.security;

import com.middleware.common.exception.CustomerServiceException;
import com.middleware.common.model.user.CustomerModel;
import com.middleware.common.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Slf4j
@Service
@RequiredArgsConstructor
class UserDetailsServiceImpl implements UserDetailsServices {
    private final CustomerRepository customerRepository;
    @Override
    public CustomerModel getJWTUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.warn("No authentication found in security context");
            throw new CustomerServiceException("USER NOT AUTHENTICATED");
        }

        Object principal = authentication.getPrincipal();
        log.info("PRINCIPAL USER TYPE: {}, VALUE: {}", principal.getClass().getSimpleName(), principal);

        // Handle different types of principals that might be returned
        if (principal instanceof CustomerModel) {
            // Direct CustomerModel (if you're storing it directly)
            return (CustomerModel) principal;
        }
        else if (principal instanceof UserDetails) {
            // Spring Security UserDetails - extract username and fetch customer
            UserDetails userDetails = (UserDetails) principal;
            return findCustomerByUsername(userDetails.getUsername());
        }
        else if (principal instanceof String) {
            // Username as string - common with JWT
            String username = (String) principal;
            return findCustomerByUsername(username);
        }
        else {
            log.error("Unsupported principal type: {}", principal.getClass().getName());
            throw new CustomerServiceException("UNSUPPORTED_TOKEN_TYPE: " + principal.getClass().getSimpleName());
        }
    }

    /**
     * Helper method to find customer by username (email in this case)
     * You'll need to implement this based on your repository/service layer
     */
    private CustomerModel findCustomerByUsername(String username) {
        try {
            // Assuming username is email based on your CustomerModel
            CustomerModel customer = customerRepository.findByEmail(username)
                    .orElseThrow(() -> new CustomerServiceException("CUSTOMER_NOT_FOUND"));


            return customer;
        } catch (Exception e) {
            log.error("Error fetching customer details for username: {}", username, e);
            throw new CustomerServiceException("ERROR_FETCHING_CUSTOMER_DETAILS");
        }
    }

    /**
     * Alternative method with more detailed error handling
     */
//    @Override
//    public CustomerModel getJWTUserDetailsWithValidation() {
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authentication == null || !authentication.isAuthenticated()) {
//                throw new CustomerServiceException("USER_NOT_AUTHENTICATED");
//            }
//
//            Object principal = authentication.getPrincipal();
//            CustomerModel customer = extractCustomerFromPrincipal(principal);
//
//            // Validate customer state
//            validateCustomerState(customer);
//
//            return customer;
//
//        } catch (CustomerServiceException e) {
//            throw e; // Re-throw custom exceptions
//        } catch (Exception e) {
//            log.error("Unexpected error in getJWTUserDetails", e);
//            throw new CustomerServiceException("AUTHENTICATION_ERROR");
//        }
//    }

//    private CustomerModel extractCustomerFromPrincipal(Object principal) {
//        if (principal instanceof CustomerModel) {
//            return (CustomerModel) principal;
//        }
//
//        String username = null;
//        if (principal instanceof UserDetails) {
//            username = ((UserDetails) principal).getUsername();
//        } else if (principal instanceof String) {
//            username = (String) principal;
//        } else {
//            throw new CustomerServiceException("UNSUPPORTED_PRINCIPAL_TYPE");
//        }
//
//        return customerRepository.findByEmail(username)
//                .orElseThrow(() -> new CustomerServiceException("CUSTOMER_NOT_FOUND"));
//    }

//    private void validateCustomerState(CustomerModel customer) {
//        if (customer == null) {
//            throw new CustomerServiceException("CUSTOMER_IS_NULL");
//        }
//
//        if (customer.getStatus() == CustomerStatus.SUSPENDED) {
//            throw new CustomerServiceException("CUSTOMER_ACCOUNT_SUSPENDED");
//        }
//
//        if (customer.getStatus() == CustomerStatus.PENDING) {
//            throw new CustomerServiceException("CUSTOMER_ACCOUNT_PENDING_VERIFICATION");
//        }
//
//        // Add other business logic validations as needed
//    }
//
//    @Override
//    public CustomerModel getJWTUserDetails() {
//        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info("PRINCIPAL USER {}", userDetails);
//        if (!(userDetails instanceof CustomerModel)) {
//            throw new CustomerServiceException("CLIENT TOKEN NOT SUPPORTED");
//        }
//        return (CustomerModel) userDetails;
//    }
}