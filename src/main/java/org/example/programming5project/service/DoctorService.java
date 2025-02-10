package org.example.programming5project.service;

import org.example.programming5project.domain.Department;
import org.example.programming5project.domain.Doctor;

import java.util.List;

public interface DoctorService {
    List<Doctor> getAllDoctors();
    Doctor findDoctorByLicenseNumber(int licenseNumber);
    void addDoctor(Doctor doctor);
    void removeDoctor(int licenseNumber);
    void assignPatientToDoctor(int doctorId, String patientId);
}
