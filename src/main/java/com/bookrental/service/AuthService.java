package com.bookrental.service;

import com.bookrental.dto.auth.AuthResponse;
import com.bookrental.dto.auth.LoginRequest;
import com.bookrental.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
}
