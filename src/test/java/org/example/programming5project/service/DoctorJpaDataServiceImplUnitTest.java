package org.example.programming5project.service;

import org.example.programming5project.domain.Doctor;
import org.example.programming5project.domain.MedicalRecord;
import org.example.programming5project.domain.Patient;
import org.example.programming5project.repository.DoctorJpaDataRepository;
import org.example.programming5project.repository.MedicalRecordRepository;
import org.example.programming5project.repository.PatientJpaDataRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class DoctorJpaDataServiceImplUnitTest {

    @Autowired
    private DoctorJpaDataServiceImpl sut;


    @MockitoBean
    private DoctorJpaDataRepository doctorJpaDataRepository;

    @MockitoBean
    private PatientJpaDataRepository patientRepository;

    @MockitoBean
    private MedicalRecordRepository medicalRecordRepository;


    @Test
    void shouldAssignPatientToDoctorIfNotAlreadyAssigned() {
        // Arrange
        int doctorId = 1;
        String patientId = "p1";

        Doctor doctor = new Doctor();
        doctor.setLicenseNumber(doctorId);
        doctor.setMedicalRecords(new ArrayList<>());

        Patient patient = new Patient();
        patient.setPatientId(patientId);

        given(doctorJpaDataRepository.findDoctorByLicenseNumber(doctorId)).willReturn(Optional.of(doctor));
        given(patientRepository.findPatientWithMedicalRecords(patientId)).willReturn(Optional.of(patient));

        // Act
        sut.assignPatientToDoctor(doctorId, patientId);

        // Assert
        ArgumentCaptor<MedicalRecord> captor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordRepository).save(captor.capture());
        assertEquals(doctor, captor.getValue().getDoctor());
        assertEquals(patient, captor.getValue().getPatient());
    }

    @Test
    void shouldNotAssignPatientToDoctorWithInvalidId() {
        // Arrange
        int doctorId = -1;
        String patientId = "p-1";

        Doctor doctor = new Doctor();
        doctor.setLicenseNumber(doctorId);
        doctor.setMedicalRecords(new ArrayList<>());

        Patient patient = new Patient();
        patient.setPatientId(patientId);

        given(doctorJpaDataRepository.findDoctorByLicenseNumber(doctorId)).willReturn(Optional.of(doctor));
        given(patientRepository.findPatientWithMedicalRecords(patientId)).willReturn(Optional.of(patient));

        // Act
        sut.assignPatientToDoctor(doctorId, patientId);

        // Assert
        ArgumentCaptor<MedicalRecord> captor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordRepository).save(captor.capture());
        assertEquals(doctor, captor.getValue().getDoctor());
        assertEquals(patient, captor.getValue().getPatient());
    }

    @Test
    void shouldAddMedicalRecordSuccessfully() {
        // Arrange
        int doctorId = 1;
        String patientId = "p123";
        LocalDate treatmentDate = LocalDate.of(2025, 5, 23);
        String diagnosis = "Flu";
        String treatment = "Rest";

        Doctor doctor = new Doctor();
        doctor.setLicenseNumber(doctorId);

        Patient patient = new Patient();
        patient.setPatientId(patientId);

        given(doctorJpaDataRepository.findDoctorByLicenseNumber(doctorId)).willReturn(Optional.of(doctor));
        given(patientRepository.findById(patientId)).willReturn(Optional.of(patient));

        // Act
        sut.addMedicalRecord(doctorId, patientId, treatmentDate, diagnosis, treatment);

        // Assert
        ArgumentCaptor<MedicalRecord> captor = ArgumentCaptor.forClass(MedicalRecord.class);
        verify(medicalRecordRepository).save(captor.capture());
        MedicalRecord saved = captor.getValue();

        assertEquals(doctor, saved.getDoctor());
        assertEquals(patient, saved.getPatient());
        assertEquals(treatmentDate, saved.getTreatmentDate());
        assertEquals(diagnosis, saved.getDiagnosis());
        assertEquals(treatment, saved.getTreatment());
    }

    @Test
    void shouldThrowExceptionWhenPatientNotFound() {
        // Arrange
        int doctorId = 7;
        String patientId = "";
        Doctor doctor = new Doctor();
        doctor.setLicenseNumber(doctorId);

        given(doctorJpaDataRepository.findDoctorByLicenseNumber(doctorId)).willReturn(Optional.of(doctor));
        given(patientRepository.findById(patientId)).willReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                sut.addMedicalRecord(doctorId, patientId, LocalDate.of(2025, 5, 23), "Fever", "Rest"));

        assertEquals("Patient not found", ex.getMessage());
        verify(medicalRecordRepository, never()).save(any());
    }

}