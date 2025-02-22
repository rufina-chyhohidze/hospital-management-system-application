package org.example.programming5project.service;

import org.example.programming5project.domain.Doctor;
import org.example.programming5project.domain.Patient;

import java.time.LocalDate;
import java.util.List;

public interface PatientService {
    List<Patient> getAllPatients();
    List<Patient> getPatientsByNameOrAdmissionDate(String name, LocalDate admissionDate);
    Patient findPatientById(String patientId);
    void addPatient(Patient patient);
    void removePatient(String patientId);
    void assignDoctorToPatient(String patientId, int doctorId);
    List<Patient> getPatientsForDoctor(int doctorId);
    List<Doctor> getDoctorsForPatient(String patientId);
    Patient findPatientWithMedicalRecords(String patientId);
    boolean updatePatientDetails(String patientId, double newBillingAmount, LocalDate newAdmissionDate);
}
