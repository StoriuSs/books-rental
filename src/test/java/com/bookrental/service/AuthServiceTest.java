package com.bookrental.service;

import com.bookrental.dto.auth.AuthResponse;
import com.bookrental.dto.auth.LoginRequest;
import com.bookrental.dto.auth.RegisterRequest;
import com.bookrental.entity.Admin;
import com.bookrental.repository.AdminRepository;
import com.bookrental.security.JwtUtils;
import com.bookrental.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setFullName("Test User");

        when(adminRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty()).thenReturn(Optional.of(new Admin()));
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtUtils.generateJwtToken(any())).thenReturn("mockJwtToken");

        AuthResponse response = authService.register(request);

        assertThat(response.getToken()).isEqualTo("mockJwtToken");
    }

    @Test
    void register_EmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");

        when(adminRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new Admin()));

        assertThrows(RuntimeException.class, () -> authService.register(request));
    }

    @Test
    void login_Success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        Admin admin = new Admin();
        admin.setEmail("test@example.com");
        admin.setFullName("Test User");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtUtils.generateJwtToken(any())).thenReturn("mockJwtToken");
        when(adminRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(admin));

        AuthResponse response = authService.login(request);

        assertThat(response.getToken()).isEqualTo("mockJwtToken");
        assertThat(response.getEmail()).isEqualTo("test@example.com");
    }
}
