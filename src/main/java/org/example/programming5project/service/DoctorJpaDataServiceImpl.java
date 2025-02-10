package org.example.programming5project.service;

import org.example.programming5project.domain.Department;
import org.example.programming5project.domain.Doctor;
import org.example.programming5project.domain.MedicalRecord;
import org.example.programming5project.domain.Patient;
import org.example.programming5project.exceptions.DoctorNotFoundException;
import org.example.programming5project.repository.DoctorJpaDataRepository;
import org.example.programming5project.repository.MedicalRecordRepository;
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
@Transactional
public class DoctorJpaDataServiceImpl implements DoctorService {
    private Logger logger = LoggerFactory.getLogger(DoctorJpaDataServiceImpl.class);

    private final DoctorJpaDataRepository doctorRepository;
    private final PatientJpaDataRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;


    public DoctorJpaDataServiceImpl(DoctorJpaDataRepository doctorRepository, PatientJpaDataRepository patientRepository, MedicalRecordRepository medicalRecordRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.medicalRecordRepository = medicalRecordRepository;
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
    @Transactional(readOnly = true) // Fetch Doctor with Medical Records
    public Doctor findDoctorByLicenseNumber(int licenseNumber) {
        return doctorRepository.findDoctorByLicenseNumber(licenseNumber)
                .orElseThrow(() -> new RuntimeException("Doctor with license number " + licenseNumber + " not found"));
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
        Doctor doctor = findDoctorByLicenseNumber(doctorId);
        Patient patient = patientRepository.findPatientWithMedicalRecords(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        boolean recordExists = doctor.getMedicalRecords().stream()
                .anyMatch(mr -> mr.getPatient().equals(patient));

        if (!recordExists) {
            MedicalRecord record = new MedicalRecord(doctor, patient);
            medicalRecordRepository.save(record);
            logger.info("Assigned patient {} to Doctor {}", patientId, doctorId);
        } else {
            logger.warn("Patient {} is already assigned to Doctor {}", patientId, doctorId);
        }
    }


}
