package com.middleware.common.security;

import com.middleware.common.model.user.CustomerModel;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

public interface UserDetailsServices {
    CustomerModel getJWTUserDetails();
}
