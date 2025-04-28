package org.example.programming5project;

import org.example.programming5project.domain.*;
import org.example.programming5project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TestHelper {
    @Autowired
    private DoctorJpaDataRepository doctorRepository;

    @Autowired
    private HospitalJpaRepository hospitalRepository;

    @Autowired
    private PatientJpaDataRepository patientRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private UserRepository userRepository;

    public Hospital createHospital(String name, String address) {
        Hospital hospital = new Hospital();
        hospital.setHospitalName(name);
        hospital.setHospitalAddress(address);
        hospital.setEstablishedDate(LocalDate.now());
        return hospitalRepository.save(hospital);
    }

    public Doctor createDoctor(String firstName, String lastName, Department department, int licenseNumber, double salary, LocalDate hireDate, Gender gender, Hospital hospital) {
        Doctor doctor = new Doctor();
        doctor.setFirstName(firstName);
        doctor.setLastName(lastName);
        doctor.setDepartment(department);
        doctor.setLicenseNumber(licenseNumber);
        doctor.setSalary(salary);
        doctor.setHireDate(hireDate);
        doctor.setGender(gender);
        doctor.setHospital(hospital);
        return doctorRepository.save(doctor);
    }

    public User createUser(String username, String password, UserRole role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setUserRole(role);
        return userRepository.save(user);
    }

    public Patient createPatient(String patientId, String firstName, String lastName, int age, Gender gender, LocalDate admissionDate, double billingAmount, User creator) {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setAge(age);
        patient.setGender(gender);
        patient.setAdmissionDate(admissionDate);
        patient.setBillingAmount(billingAmount);
        patient.setCreator(creator);
        return patientRepository.save(patient);
    }

    public MedicalRecord createMedicalRecord(Doctor doctor, Patient patient, LocalDate treatmentDate, String diagnosis, String treatment) {
        MedicalRecord record = new MedicalRecord();
        record.setDoctor(doctor);
        record.setPatient(patient);
        record.setTreatmentDate(treatmentDate);
        record.setDiagnosis(diagnosis);
        record.setTreatment(treatment);
        return medicalRecordRepository.save(record);
    }
    public Patient createPatientWithoutSaving(String patientId, String firstName, String lastName, int age, Gender gender, LocalDate admissionDate, double billingAmount, User creator) {
        Patient patient = new Patient();
        patient.setPatientId(patientId);
        patient.setFirstName(firstName);
        patient.setLastName(lastName);
        patient.setAge(age);
        patient.setGender(gender);
        patient.setAdmissionDate(admissionDate);
        patient.setBillingAmount(billingAmount);
        patient.setCreator(creator);
        return patient;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }
    public PatientJpaDataRepository getPatientRepository() {
        return patientRepository;
    }

    public MedicalRecordRepository getMedicalRecordRepository() {
        return medicalRecordRepository;
    }

    public void cleanUp() {
        medicalRecordRepository.deleteAll();
        doctorRepository.deleteAll();
        hospitalRepository.deleteAll();
        patientRepository.deleteAll();
        userRepository.deleteAll();
    }
}
