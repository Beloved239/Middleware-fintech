package com.middleware.common.dto;

import lombok.Data;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@Data
public class AuthRequest {
   private String emailOrPhoneNumber;
   private String password;
}
