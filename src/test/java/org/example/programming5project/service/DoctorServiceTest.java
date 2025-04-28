package org.example.programming5project.service;

import org.example.programming5project.TestHelper;
import org.example.programming5project.domain.*;
import org.example.programming5project.repository.DoctorJpaDataRepository;
import org.example.programming5project.repository.MedicalRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DoctorServiceTest {

    @Autowired
    private DoctorJpaDataServiceImpl sut;

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private DoctorJpaDataRepository doctorRepository;



    @Test
    void shouldDeleteDoctorAndCascadeDeleteMedicalRecords() {
        Hospital hospital = testHelper.createHospital("Cascade Hospital", "Cascade Street");
        Doctor doctor = testHelper.createDoctor("Derek", "Shepherd", Department.CARDIOLOGY, 77777, 14000, LocalDate.now(), Gender.MALE, hospital);
        User creator = testHelper.createUser("Rufi", "Chy", UserRole.ADMIN);
        Patient patient = testHelper.createPatient("P777", "Anna", "Werite", 32, Gender.FEMALE, LocalDate.now(), 600.0, creator);

        MedicalRecord record = testHelper.createMedicalRecord(doctor, patient, LocalDate.now(), "Head Injury", "Surgery");

        sut.removeDoctor(doctor.getLicenseNumber());

        assertFalse(doctorRepository.findById(doctor.getLicenseNumber()).isPresent(), "Doctor should be deleted");
        assertTrue(medicalRecordRepository.findAll().isEmpty(), "Medical Records should be deleted with Doctor");
    }

    @Test
    void shouldRemoveDoctorSuccessfully() {
        Hospital hospital = testHelper.createHospital("Hospital Remove", "Address 5");
        Doctor doctor = testHelper.createDoctor("Fran", "Aghemio", Department.RADIOLOGY, 55555, 13000, LocalDate.now(), Gender.MALE, hospital);

        sut.removeDoctor(doctor.getLicenseNumber());

        assertFalse(doctorRepository.findById(doctor.getLicenseNumber()).isPresent());
    }
    @Test
    void shouldNotAddMedicalRecordWhenPatientNotFound() {
        // Arrange
        Hospital hospital = testHelper.createHospital("Antwerp Central Hospital", "Some Address");
        Doctor doctor = testHelper.createDoctor("Rufina", "Chy", Department.CARDIOLOGY, 66666, 15000, LocalDate.now(), Gender.FEMALE, hospital);

        //you can change it here to P777 and it will fail the test
        assertThrows(RuntimeException.class, () ->
                sut.addMedicalRecord(doctor.getLicenseNumber(), "Unknow id", LocalDate.now(), "Diagnosis", "Treatment")
        );
    }

    @Test
    void shouldThrowWhenFindingNonExistingDoctorByLicenseNumber() {
        int nonExistingLicenseNumber = 99999;

        // and you can insert 66666, the existing one
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sut.findDoctorByLicenseNumber(nonExistingLicenseNumber);
        });
        assertTrue(exception.getMessage().contains("Doctor with license number"));
    }


}