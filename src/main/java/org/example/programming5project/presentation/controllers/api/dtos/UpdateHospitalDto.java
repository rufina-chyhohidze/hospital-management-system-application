package org.example.programming5project.presentation.controllers.api.dtos;

import java.time.LocalDate;

public record UpdateHospitalDto(
        String hospitalName,
        String hospitalAddress,
        LocalDate establishedDate
) {}