package org.example.programming5project.presentation.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.programming5project.domain.Gender;

import java.time.LocalDate;

public record AddPatientDto(
        String patientId,

        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 30)
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 30)
        String lastName,

        @Min(value = 1, message = "Age must be at least 1")
        int age,

        @NotNull(message = "Gender is required")
        Gender gender,

        @NotNull
        LocalDate admissionDate,
        @NotNull
        double billingAmount
) {
}
