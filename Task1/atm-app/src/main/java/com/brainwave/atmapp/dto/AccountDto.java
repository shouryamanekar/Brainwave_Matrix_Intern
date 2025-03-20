package com.brainwave.atmapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record AccountDto(
        Long id,

        @NotBlank(message = "Account holder name cannot be blank")
        String accountHolderName,

        @PositiveOrZero(message = "Balance should not be negative")
        double balance,

        String atmCardNumber
) {}