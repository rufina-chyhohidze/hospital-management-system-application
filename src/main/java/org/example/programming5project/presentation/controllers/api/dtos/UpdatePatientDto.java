package org.example.programming5project.presentation.controllers.api.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdatePatientDto(@NotNull double billingAmount,
                               @NotNull LocalDate admissionDate) {
}
