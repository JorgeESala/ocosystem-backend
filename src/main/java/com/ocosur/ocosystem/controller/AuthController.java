package com.ocosur.ocosystem.controller;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.dto.AuthRequest;
import com.ocosur.ocosystem.dto.AuthResponse;
import com.ocosur.ocosystem.dto.RegisterRequest;
import com.ocosur.ocosystem.security.AuthService;
import com.ocosur.ocosystem.security.dto.UserSessionDTO;

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
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest req) {

        var auth = svc.login(
                req.email().toLowerCase().trim(),
                req.password());

        Employee user = auth.getEmployee();

        UserSessionDTO userDto = UserSessionDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .allowedBusinesses(
                        user.getBusinesses()
                                .stream()
                                .map(eb -> eb.getBusinessType().getCode())
                                .toList())
                .build();

        return ResponseEntity.ok(
                new AuthResponse(auth.getToken(), userDto));
    }

}
