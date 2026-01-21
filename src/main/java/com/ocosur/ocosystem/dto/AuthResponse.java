package com.ocosur.ocosystem.dto;

import com.ocosur.ocosystem.security.dto.UserSessionDTO;

public record AuthResponse(
        String token,
        UserSessionDTO user) {
}
