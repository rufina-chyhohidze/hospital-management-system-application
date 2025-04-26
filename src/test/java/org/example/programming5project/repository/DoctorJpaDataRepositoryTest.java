package org.example.programming5project.repository;

import org.example.programming5project.TestHelper;
import org.example.programming5project.domain.Department;
import org.example.programming5project.domain.Doctor;
import org.example.programming5project.domain.Gender;
import org.example.programming5project.domain.Hospital;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class DoctorJpaDataRepositoryTest {

    @Autowired
    private DoctorJpaDataRepository sut;

    @Autowired
    private TestHelper testHelper;

    @Test
    void shouldFindDoctorByLicenseNumber_success() {
        Hospital hospital = testHelper.createHospital("Antwerp Hospital", "Pothoekstraat 122");
        Doctor doctor = testHelper.createDoctor("Alice", "Soloviy", Department.CARDIOLOGY, 98765, 6000.0, LocalDate.of(2018, 5, 15), Gender.FEMALE, hospital);

        Optional<Doctor> retrievedDoctor = sut.findDoctorByLicenseNumber(98765);

        assertTrue(retrievedDoctor.isPresent());
        assertEquals("Alice", retrievedDoctor.get().getFirstName());
        assertEquals(98765, retrievedDoctor.get().getLicenseNumber());
        assertNotNull(retrievedDoctor.get().getHospital());
    }

    @Test
    void shouldReturnEmptyList_whenNoDoctorForPatient() {
        // no doctor assigned to this patient
        var doctors = sut.findDoctorsForPatient("NON_EXISTENT_PATIENT_ID");
        assertTrue(doctors.isEmpty());
    }

    @Test
    void shouldFindDoctorForPatient() {
        Hospital hospital = testHelper.createHospital("Brussels Dentistry center", "Avenie de Roode 23");
        Doctor doctor = testHelper.createDoctor(
                "Sarah", "Connor", Department.DENTISTRY,
                2222, 6000.0, LocalDate.of(2019, 5, 15), Gender.FEMALE, hospital
        );

        var patient = testHelper.createPatient(
                "P1000", "Kyle", "Reese", 35, Gender.MALE,
                LocalDate.of(2025, 5, 5), 700.0, null
        );

        testHelper.createMedicalRecord(doctor, patient, LocalDate.now(), "Test Diagnosis", "Test Treatment");
        var foundDoctors = sut.findDoctorsForPatient(patient.getPatientId());

        assertFalse(foundDoctors.isEmpty()); //means that doctor was assigned
        assertEquals(doctor.getLicenseNumber(), foundDoctors.get(0).getLicenseNumber());
    }
    @Test
    void shouldReturnEmptyListWhenNoDoctorForPatient() {
        var hospital = testHelper.createHospital("Amsterdam Cental Hospital", "Straat 23");
        testHelper.createDoctor("Henry", "Smith", Department.CARDIOLOGY,
                3333, 8000.0, LocalDate.of(2018, 3, 10), Gender.MALE, hospital);

        var foundDoctors = sut.findDoctorsForPatient("NON_EXISTENT_PATIENTID3");

        assertTrue(foundDoctors.isEmpty());
    }
}