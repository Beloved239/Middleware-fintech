package com.middleware.common.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Customer Entity representing fintech customers
 * Contains customer personal information and verification status
 *
 * @author Oluwatobi Adebanjo
 */

@Entity
@Table(name = "customers")
@Data
@Builder
@AllArgsConstructor
public class CustomerModel implements Serializable {
    // Constructors
    public CustomerModel() {
        this.createdAt = LocalDateTime.now();
        this.status = CustomerStatus.PENDING;
    }

    public CustomerModel(String firstName, String lastName, String email, String phoneNumber) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Column(unique = true, nullable = false)
    @Email(message = "Valid email is required")
    private String email;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^\\+234[0-9]{10}$", message = "Valid Nigerian phone number is required (+234XXXXXXXXXX)")
    private String phoneNumber;

    @Column(unique = true)
    @Pattern(regexp = "^[0-9]{11}$", message = "BVN must be 11 digits")
    private String bvn;

    @Column(unique = true)
    @Pattern(regexp = "^[0-9]{11}$", message = "NIN must be 11 digits")
    private String nin;

    private String address;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private CustomerStatus status = CustomerStatus.PENDING;

    @Builder.Default
    private Boolean bvnVerified = false;

    @Builder.Default
    private Boolean ninVerified = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @UpdateTimestamp
    private LocalDateTime verifiedAt;

    private String password;

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getBvn() { return bvn; }
    public void setBvn(String bvn) { this.bvn = bvn; }

    public String getNin() { return nin; }
    public void setNin(String nin) { this.nin = nin; }

    public CustomerStatus getStatus() { return status; }
    public void setStatus(CustomerStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Boolean getBvnVerified() { return bvnVerified; }
    public void setBvnVerified(Boolean bvnVerified) { this.bvnVerified = bvnVerified; }

    public Boolean getNinVerified() { return ninVerified; }
    public void setNinVerified(Boolean ninVerified) { this.ninVerified = ninVerified; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public LocalDateTime getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(LocalDateTime verifiedAt) { this.verifiedAt = verifiedAt; }

    public void  setPassword(String passWord){
        this.password = passWord;
    }
    public String getPassword(){
        return password;
    }

}
