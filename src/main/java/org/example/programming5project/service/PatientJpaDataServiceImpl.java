package org.example.programming5project.service;

import org.example.programming5project.domain.Doctor;
import org.example.programming5project.domain.Patient;
import org.example.programming5project.repository.DoctorJpaDataRepository;
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
        //("patientJpaDataServiceImpl")
@Profile("jpa")
public class PatientJpaDataServiceImpl implements PatientService {
    private Logger logger = LoggerFactory.getLogger(PatientJpaDataServiceImpl.class);

    private final PatientJpaDataRepository patientRepository;
    private final DoctorJpaDataRepository doctorRepository;

    public PatientJpaDataServiceImpl(PatientJpaDataRepository patientRepository, DoctorJpaDataRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
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
    public Patient findPatientById(String patientId) {
        return patientRepository.findById(patientId).orElse(null);
    }

    @Override
    public void addPatient(Patient patient) {
        patientRepository.save(patient);
    }

    @Override
    @Transactional
    public void removePatient(String patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Break associations
        for (Doctor doctor : patient.getDoctors()) {
            doctor.getPatients().remove(patient);
            doctorRepository.save(doctor); // Save the doctor after modification
        }
        logger.info("Patient " + patientId + " removed");
        patientRepository.delete(patient);
    }

    @Override
    @Transactional
    public void assignDoctorToPatient(String patientId, int doctorId) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new RuntimeException("Patient not found"));
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));

        // Update both sides of the relationship
        patient.getDoctors().add(doctor);
        doctor.getPatients().add(patient);

        // Save the owning side (Doctor)
        doctorRepository.save(doctor);
        logger.info("Doctor " + doctorId + " assigned to patient " + patientId);
    }

    @Override
    public List<Patient> getPatientsForDoctor(int doctorId) {
        return patientRepository.findPatientsForDoctor(doctorId);
    }

    @Override
    public List<Doctor> getDoctorsForPatient(String patientId) {
        return doctorRepository.findDoctorsForPatient(patientId);
    }

}
