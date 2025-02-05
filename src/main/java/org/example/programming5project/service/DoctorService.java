package org.example.programming5project.service;

import org.example.programming5project.domain.Department;
import org.example.programming5project.domain.Doctor;

import java.util.List;

public interface DoctorService {
    List<Doctor> getAllDoctors();

    List<Doctor> getDoctorsByDepartment(Department department);

    Doctor findDoctorByLicenseNumber(int licenseNumber);

    void addDoctor(Doctor doctor);
    void removeDoctor(int licenseNumber);
    List<Doctor> getDoctorsForPatient(String patientId);
    void assignPatientToDoctor(int doctorId, String patientId);
}
