package com.ocosur.ocosystem.controller;

import com.ocosur.ocosystem.dto.AuthRequest;
import com.ocosur.ocosystem.dto.AuthResponse;
import com.ocosur.ocosystem.dto.RegisterRequest;
import com.ocosur.ocosystem.security.AuthService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    @Value("${app.registration.enabled:false}")
    private boolean registrationEnabled;

    private final AuthService svc;

    public AuthController(AuthService svc) {
        this.svc = svc;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        if (!registrationEnabled) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Registro deshabilitado");
        }
        var resp = svc.register(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        var resp = svc.login(req.email().toLowerCase().trim(), req.password());
        return ResponseEntity.ok(resp);
    }
}
