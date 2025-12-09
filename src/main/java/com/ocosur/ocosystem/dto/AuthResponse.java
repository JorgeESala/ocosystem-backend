package com.ocosur.ocosystem.dto;

public record AuthResponse(String token, Long userId, String email, String name, String role) {
}
