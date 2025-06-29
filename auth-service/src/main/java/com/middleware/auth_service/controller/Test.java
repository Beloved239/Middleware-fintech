package com.middleware.auth_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/api/v1/test")
public class Test {
    @GetMapping(path = "/welcome")
    public String welcome(){
        return "Welcome to a new world";
    }
}
