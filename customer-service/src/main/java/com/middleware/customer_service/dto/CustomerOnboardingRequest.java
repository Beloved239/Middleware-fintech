package com.middleware.customer_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for customer onboarding request
 * Contains all required information for customer registration
 *
 * @author Oluwatobi Adebanjo
 */
@Data
public class CustomerOnboardingRequest {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+234[0-9]{10}$", message = "Valid Nigerian phone number is required (+234XXXXXXXXXX)")
    private String phoneNumber;

    @Pattern(regexp = "^[0-9]{11}$", message = "BVN must be 11 digits")
    private String bvn;

    @Pattern(regexp = "^[0-9]{11}$", message = "NIN must be 11 digits")
    private String nin;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
