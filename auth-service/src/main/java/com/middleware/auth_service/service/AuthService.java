package com.middleware.auth_service.service;

import com.middleware.common.security.JwtTokenProvider;
import com.middleware.common.dto.AuthRequest;
import com.middleware.common.dto.MiddleWareResponse;
import com.middleware.common.exception.CustomerServiceException;
import com.middleware.common.model.user.CustomerModel;
import com.middleware.common.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final CustomerRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;


    public MiddleWareResponse login(AuthRequest request) {
        log.info("Login request: {}", request);
        String token;
        Optional<CustomerModel> use = userRepository.findByPhoneNumber(request.getEmailOrPhoneNumber());
        log.info("Cusomer>>>{}", use.get().getPhoneNumber());
        CustomerModel user = userRepository.findByPhoneNumber(request.getEmailOrPhoneNumber())
                .orElseThrow(() -> new CustomerServiceException("User Not Found"));
        log.info("User found: {}", user);
        log.info("Password found: {}", passwordEncoder.encode(request.getPassword()));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomerServiceException("Invalid email/password supplied");
        }
        log.info("About to generate token");

        token = tokenProvider.generateToken(user);

        return new MiddleWareResponse(true, "TokenGenerated Successfully",  token, LocalDateTime.now());
    }

}
