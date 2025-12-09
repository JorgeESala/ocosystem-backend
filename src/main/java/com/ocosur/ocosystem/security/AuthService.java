package com.ocosur.ocosystem.security;

import com.ocosur.ocosystem.dto.AuthResponse;
import com.ocosur.ocosystem.dto.RegisterRequest;
import com.ocosur.ocosystem.model.Employee;
import com.ocosur.ocosystem.repository.EmployeeRepository;
import com.ocosur.ocosystem.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmployeeRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    public AuthService(EmployeeRepository repo, PasswordEncoder encoder, JwtService jwtService,
            AuthenticationManager authManager) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    public AuthResponse register(RegisterRequest req) {
        if (repo.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        Employee e = Employee.builder()
                .name(req.name())
                .email(req.email())
                .passwordHash(encoder.encode(req.password()))
                .role("USER")
                .active(true)
                .build();

        e = repo.save(e);
        String token = jwtService.generateToken(e.getEmail(), e.getRole());
        return new AuthResponse(token, e.getId(), e.getEmail(), e.getName(), e.getRole());
    }

    public AuthResponse login(String email, String password) {
        // esto lanzará BadCredentialsException si no coincide
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        Employee e = repo.findByEmail(email).orElseThrow();
        String token = jwtService.generateToken(e.getEmail(), e.getRole());
        return new AuthResponse(token, e.getId(), e.getEmail(), e.getName(), e.getRole());
    }
}
