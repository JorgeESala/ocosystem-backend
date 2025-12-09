package com.ocosur.ocosystem.controller;

import com.ocosur.ocosystem.dto.AuthRequest;
import com.ocosur.ocosystem.dto.AuthResponse;
import com.ocosur.ocosystem.dto.RegisterRequest;
import com.ocosur.ocosystem.security.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService svc;

    public AuthController(AuthService svc) {
        this.svc = svc;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        var resp = svc.register(req);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        var resp = svc.login(req.email(), req.password());
        return ResponseEntity.ok(resp);
    }
}
