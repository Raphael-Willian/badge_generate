package com.example.badge.generate.records.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull(message = "O email é obrigatório.")
        @NotBlank(message = "O email é obrigatório.")
        String email,

        @NotNull(message = "A senha é obrigatória.")
        @NotBlank(message = "A senha é obrigatória.")
        String password
) {
}
