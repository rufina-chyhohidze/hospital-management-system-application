package org.example.programming5project.service;

import org.example.programming5project.domain.Department;
import org.example.programming5project.domain.Doctor;
import org.example.programming5project.domain.Patient;
import org.example.programming5project.exceptions.DoctorNotFoundException;
import org.example.programming5project.repository.DoctorJpaDataRepository;
import org.example.programming5project.repository.PatientJpaDataRepository;
import org.example.programming5project.service.DoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Uses JpaDataRepositories
 */
@Service
        //("doctorJpaDataServiceImpl")
@Profile("jpa")
public class DoctorJpaDataServiceImpl implements DoctorService {
    private Logger logger = LoggerFactory.getLogger(DoctorJpaDataServiceImpl.class);

    private final DoctorJpaDataRepository doctorRepository;
    private final PatientJpaDataRepository patientRepository;


    public DoctorJpaDataServiceImpl(DoctorJpaDataRepository doctorRepository, PatientJpaDataRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public List<Doctor> getDoctorsByDepartment(Department department) {
        return doctorRepository.findByDepartment(department);
    }

    @Override
    public Doctor findDoctorByLicenseNumber(int licenseNumber) {
        return doctorRepository.findById(licenseNumber).orElse(null);
    }

    @Override
    public void addDoctor(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    @Override
    public void removeDoctor(int licenseNumber) {
        doctorRepository.deleteById(licenseNumber);
    }

    @Override
    public List<Doctor> getDoctorsForPatient(String patientId) {
        return doctorRepository.findDoctorsForPatient(patientId);
    }

    @Override
    @Transactional
    public void assignPatientToDoctor(int doctorId, String patientId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new DoctorNotFoundException("Doctor with license number " + doctorId + " not found"));
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));

        // Update both sides of the relationship
        doctor.getPatients().add(patient);
        patient.getDoctors().add(doctor);

        // Save the owning side (Doctor)
        doctorRepository.save(doctor);
        logger.info("Assigned patient " + patientId + " to Doctor " + doctorId);

    }
}
