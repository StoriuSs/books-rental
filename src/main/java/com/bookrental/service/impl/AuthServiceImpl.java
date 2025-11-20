package com.bookrental.service.impl;

import com.bookrental.dto.auth.AuthResponse;
import com.bookrental.dto.auth.LoginRequest;
import com.bookrental.dto.auth.RegisterRequest;
import com.bookrental.entity.Admin;
import com.bookrental.repository.AdminRepository;
import com.bookrental.security.JwtUtils;
import com.bookrental.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        Admin admin = adminRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Error: Admin not found."));

        return new AuthResponse(jwt, "Bearer", admin.getEmail(), admin.getFullName());
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (adminRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Error: Email is already taken!");
        }

        Admin admin = new Admin();
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setFullName(request.getFullName());

        adminRepository.save(admin);

        // Auto login after register
        return login(new LoginRequest() {{
            setEmail(request.getEmail());
            setPassword(request.getPassword());
        }});
    }
}
