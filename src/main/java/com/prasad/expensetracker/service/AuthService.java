package com.prasad.expensetracker.service;

import com.prasad.expensetracker.dto.LoginRequest;
import com.prasad.expensetracker.dto.LoginResponse;
import com.prasad.expensetracker.dto.RegisterRequest;
import com.prasad.expensetracker.dto.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

}