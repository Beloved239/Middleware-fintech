package com.middleware.auth_service.controller;

import com.middleware.auth_service.service.AuthService;
import com.middleware.common.dto.AuthRequest;
import com.middleware.common.dto.MiddleWareResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * Middle-ware Fintech Solution
 *
 * @author: Oluwatobi Adebanjo
 * @Date: 29/06/2025
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public MiddleWareResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }
}
