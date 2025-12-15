package com.ocosur.ocosystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeCredentialsRequest(

        @NotBlank String name,

        @Email @NotBlank String email,

        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String newPassword) {
}
