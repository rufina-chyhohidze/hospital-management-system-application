package org.example.programming5project.service;

import org.example.programming5project.domain.Doctor;
import org.example.programming5project.domain.MedicalRecord;
import org.example.programming5project.domain.Patient;
import org.example.programming5project.exceptions.PatientNotFoundException;
import org.example.programming5project.repository.DoctorJpaDataRepository;
import org.example.programming5project.repository.MedicalRecordRepository;
import org.example.programming5project.repository.PatientJpaDataRepository;
import org.example.programming5project.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Uses JpaDataRepositories
 */
@Service
@Profile("jpa")
@Transactional
public class PatientJpaDataServiceImpl implements PatientService {
    private Logger logger = LoggerFactory.getLogger(PatientJpaDataServiceImpl.class);

    private final PatientJpaDataRepository patientRepository;
    private final DoctorJpaDataRepository doctorRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public PatientJpaDataServiceImpl(PatientJpaDataRepository patientRepository, DoctorJpaDataRepository doctorRepository, MedicalRecordRepository medicalRecordRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }


    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public List<Patient> getPatientsByNameOrAdmissionDate(String name, LocalDate admissionDate) {
        return patientRepository.findByNameOrAdmissionDate(name, admissionDate);
    }

    @Override
    @Transactional // Defines Transaction Boundary
    public Patient findPatientById(String patientId) {
        return patientRepository.findPatientWithMedicalRecords(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient with ID " + patientId + " not found"));
    }


    @Override
    public void addPatient(Patient patient) {
        patientRepository.save(patient);
    }

    @Override
    @Transactional
    public void removePatient(String patientId) {
        Patient patient = findPatientById(patientId);
        medicalRecordRepository.deleteByPatient(patient);
        patientRepository.delete(patient);
        logger.info("Patient {} removed", patientId);
    }


    @Override
    @Transactional
    public void assignDoctorToPatient(String patientId, int doctorId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findDoctorByLicenseNumber(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        boolean recordExists = patient.getMedicalRecords().stream()
                .anyMatch(mr -> mr.getDoctor().equals(doctor));

        if (!recordExists) {
            MedicalRecord record = new MedicalRecord(doctor, patient);
            medicalRecordRepository.save(record);
            logger.info("Doctor {} assigned to patient {}", doctorId, patientId);
        } else {
            logger.warn("Doctor {} is already assigned to patient {}", doctorId, patientId);
        }
    }

    @Override
    @Transactional(readOnly = true)//LAZY FETCH
    public List<Patient> getPatientsForDoctor(int doctorId) {
        return patientRepository.findPatientsForDoctor(doctorId);
    }

    @Override
    public List<Doctor> getDoctorsForPatient(String patientId) {
        return doctorRepository.findDoctorsForPatient(patientId);
    }

    @Override
    @Transactional(readOnly = true)
    public Patient findPatientWithMedicalRecords(String patientId) {
        return patientRepository.findPatientWithMedicalRecords(patientId)
                .orElseThrow(() -> new PatientNotFoundException("Patient with ID " + patientId + " not found"));
    }

}
