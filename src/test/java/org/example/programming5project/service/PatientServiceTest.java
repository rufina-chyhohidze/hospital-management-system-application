package org.example.programming5project.service;

import org.example.programming5project.TestHelper;
import org.example.programming5project.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PatientServiceTest {

    @Autowired
    private PatientJpaDataServiceImpl sut;

    @Autowired
    private TestHelper testHelper;

    @Test
    void removingPatientShouldAlsoRemoveMedicalRecords() {
        Hospital hospital = testHelper.createHospital("Antwerp city hospital", "Nationalstraat 12");
        Doctor doctor = testHelper.createDoctor("Tom", "Hardy", Department.DENTISTRY, 33333, 18000, LocalDate.now(), Gender.MALE, hospital);
        User creator = testHelper.createUser("tomhardyuser", "pass", UserRole.USER);
        Patient patient = testHelper.createPatient("PAT004", "Robert", "Downey", 50, Gender.MALE, LocalDate.now(), 800, creator);

        testHelper.createMedicalRecord(doctor, patient, LocalDate.now(), "Cancer", "Chemo");

        sut.removePatient(patient.getPatientId());

        assertFalse(testHelper.getPatientRepository().findById(patient.getPatientId()).isPresent(), "Patient should be deleted");
        assertTrue(testHelper.getMedicalRecordRepository().findAll().isEmpty(), "Associated medical records should also be deleted");
    }

    @Test
    void shouldAllowMultipleMedicalRecordsWithSameDoctor() {
        var hospital = testHelper.createHospital("Tunis city hospital", "Nationalstraat 12");
        var doctor = testHelper.createDoctor(
                "Rufina", "Chyhohidze",
                Department.CARDIOLOGY,
                1001, 80000.0,
                LocalDate.of(2020, 1, 1),
                Gender.MALE,
                hospital
        );

        var creator = testHelper.createUser("creator1", "password", UserRole.USER);
        var patient = testHelper.createPatient(
                "P001", "Eva", "Reichel",
                21, Gender.FEMALE,
                LocalDate.of(2024, 4, 28),
                1200.00,
                creator
        );

        testHelper.createMedicalRecord(doctor, patient, LocalDate.of(2024, 4, 28), "Diagnosis 1", "Treatment 1");
        testHelper.createMedicalRecord(doctor, patient, LocalDate.of(2024, 5, 5), "Diagnosis 2", "Treatment 2");

        var refreshedPatient = sut.findPatientWithMedicalRecords(patient.getPatientId());

        assertEquals(2, refreshedPatient.getMedicalRecords().size(),
                "Patient CAN have two different medical records with the same doctor");
    }

    @Test
    void shouldFailToUpdateNonExistingPatient() {
        String nonExistingPatientId = "0000"; //this one not exists

        boolean updated = sut.updatePatientDetails(nonExistingPatientId, 1500.0, LocalDate.now());

        assertFalse(updated, "Updating a non-existing patient should fail");
    }


}