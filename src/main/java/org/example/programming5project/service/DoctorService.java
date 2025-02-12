package org.example.programming5project.service;

import org.example.programming5project.domain.Department;
import org.example.programming5project.domain.Doctor;

import java.time.LocalDate;
import java.util.List;

public interface DoctorService {
    List<Doctor> getAllDoctors();
    Doctor findDoctorByLicenseNumber(int licenseNumber);
    void addDoctor(Doctor doctor);
    void removeDoctor(int licenseNumber);
    void assignPatientToDoctor(int doctorId, String patientId);
    void addMedicalRecord(int doctorId, String patientId, LocalDate treatmentDate, String diagnosis, String treatment);

}
