package org.example.programming5project.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Patient (Many-to-Many with Doctor)
 * A Patient can have many Doctors assigned. Each Doctor can have many Patients.
 */
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @Column(name = "patient_id", unique = true, nullable = false)
    private String patientId; // Patient's unique identifier (primary key)

    private String firstName;
    private String lastName;
    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private double billingAmount;

    private LocalDate admissionDate;

    @OneToMany(mappedBy = "patient", fetch = FetchType.LAZY)
    private List<MedicalRecord> medicalRecords = new ArrayList<>();
    // Many-to-One relationship with Hospital
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = true) // Nullable hospital association
    private Hospital hospital;

    public Patient(String firstName, String lastName, int age, Gender gender, String patientId, double billingAmount, LocalDate admissionDate,Hospital hospital) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.patientId = patientId;
        this.billingAmount = billingAmount;
        this.admissionDate = admissionDate;
       // this.doctors = new HashSet<>();
        this.hospital = hospital;
    }

    public Patient(){

    }
    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    /**
     * Default Constructor
     */
    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getPatientId() {
        return patientId;
    }
    public double getBillingAmount() {
        return billingAmount;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }
//
//    // Many-to-Many Relationship Methods
//
//    /**
//     * Method that adds a doctor to a patient's list ensuring bidirectional relationship
//     *
//     * @param doctor The Doctor to be added
//     */
//    public void addDoctor(Doctor doctor) {
//        if (doctor == null) {
//            throw new IllegalArgumentException("Doctor cannot be null.");
//        }
//        if (!doctors.contains(doctor)) {
//            doctors.add(doctor);
//            doctor.addPatient(this); // Bidirectional addition
//        }
//    }
//
//    /**
//     * Method to remove a doctor from a patient's list
//     *
//     * @param doctor The Doctor to be removed
//     */
//    public void removeDoctor(Doctor doctor) {
//        if (doctors.contains(doctor)) {
//            doctors.remove(doctor);
//            doctor.removePatient(this); // Bidirectional removal
//        }
//    }

    // toString() Method
    @Override
    public String toString() {
        return "Patient{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", patientId='" + patientId + '\'' +
                ", billingAmount=" + billingAmount +
                ", admissionDate=" + admissionDate +
                ", doctors=" + medicalRecords.size() + " medical records" +
                '}';
    }

    /**
     * Overriding equals method based on patientId.
     *
     * @param o Object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(patientId, patient.patientId);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBillingAmount(double billingAmount) {
        this.billingAmount = billingAmount;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }


    /**
     * Overriding hashCode method based on patientId.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(patientId);
    }
}