package com.prasad.expensetracker.controller;

import com.prasad.expensetracker.dto.LoginRequest;
import com.prasad.expensetracker.dto.LoginResponse;
import com.prasad.expensetracker.dto.RegisterRequest;
import com.prasad.expensetracker.dto.RegisterResponse;
import com.prasad.expensetracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {

        return authService.register(request);

    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        return authService.login(request);

    }
}