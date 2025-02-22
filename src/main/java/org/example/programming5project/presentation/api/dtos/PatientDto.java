package org.example.programming5project.presentation.api.dtos;

import org.example.programming5project.domain.Gender;
import org.example.programming5project.domain.Patient;

import java.time.LocalDate;

public record PatientDto(String patientId,
                         String firstName,
                         String lastName,
                         int age,
                         Gender gender,
                         LocalDate admissionDate,
                         double billingAmount) {

    public static PatientDto fromEntity(Patient patient) {
        return new PatientDto(
                patient.getPatientId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getAge(),
                patient.getGender(),
                patient.getAdmissionDate(),
                patient.getBillingAmount()
        );
    }
}
