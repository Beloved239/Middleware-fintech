package com.middleware.common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

public interface CustomUserDetailsService extends UserDetailsService {
    UserDetails loadUserByUsername(String username);
    UserDetails loadUserById(UUID id);
}
