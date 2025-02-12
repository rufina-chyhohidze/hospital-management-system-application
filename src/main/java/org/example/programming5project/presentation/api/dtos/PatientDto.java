package org.example.programming5project.presentation.api.dtos;

import org.example.programming5project.domain.Patient;

import java.time.LocalDate;

public record PatientDto(String patientId,
                         String firstName,
                         String lastName,
                         int age,
                         LocalDate admissionDate,
                         double billingAmount) {

    //fromEntity method converts a Patient entity into a PatientDto.
    public static PatientDto fromEntity(Patient patient) {
        return new PatientDto(
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getAge(),
                patient.getAdmissionDate(),
                patient.getBillingAmount()
        );
    }
}
