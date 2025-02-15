package org.example.programming5project.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

/**
 * Doctor (Many-to-Many with Patient)
 * A Doctor can have many Patients. Each Patient can have many Doctors assigned.
 * Hospital (One-to-Many with Doctor)
 * A Hospital can employ many Doctors. Each Doctor works in only one Hospital.
 */
@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @Column(name = "license_number", unique = true, nullable = false)
    private int licenseNumber; // Doctor's unique identifier (primary key)

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Department department;

    private double salary;

    private LocalDate hireDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    // Many-to-Many relationship with Patient
   // @ManyToMany( cascade = {CascadeType.PERSIST,CascadeType.DETACH,CascadeType.REFRESH, CascadeType.MERGE})

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY)
    private List<MedicalRecord> medicalRecords = new ArrayList<>();
   // private Set<Patient> patients = new HashSet<>();

    // Many-to-One relationship with Hospital
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = true)
    private Hospital hospital;


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setLicenseNumber(int licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }


    public Doctor(String firstName, String lastName, Department department, int licenseNumber, double salary, LocalDate hireDate, Gender gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.licenseNumber = licenseNumber;
        this.salary = salary;
        this.hireDate = hireDate;
        this.gender = gender;
       // this.patients = new HashSet<>();
    }

    public Doctor() {

    }

    //constructor for addition a doctor
    public Doctor(String firstName, String lastName, int licenseNumber, Department department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.licenseNumber = licenseNumber;
        this.department = department;
       // this.patients = new HashSet<>(); //for patient initialisation
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Department getDepartment() {
        return department;
    }

    public int getLicenseNumber() {
        return licenseNumber;
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public Gender getGender() {
        return gender;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

//
//    // Many-to-Many Relationship Methods
//
//    /**
//     * static method to add a patient to the doctor's list
//     * for usage in DataFACTORY
//     * @param patient The Patient to be added
//     */
//    public void addPatient(Patient patient) {
//        if (patient == null) {
//            throw new IllegalArgumentException("Patient cannot be null.");
//        }
//        if (!patients.contains(patient)) {
//            patients.add(patient);
//            patient.addDoctor(this); // bidirectional addition
//        }
//    }
//
//    /**
//     * static method to remove a patient from the doctor's list
//     * to use in DataFactory
//     * @param patient The Patient to be removed
//     */
//    public void removePatient(Patient patient) {
//        if (patients.contains(patient)) {
//            patients.remove(patient);
//            patient.removeDoctor(this); // bidirectional removal
//        }
//    }

    // toString() Method
    @Override
    public String toString() {
        return "Doctor{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", department=" + department +
                ", licenseNumber=" + licenseNumber +
                ", salary=" + salary +
                ", hireDate=" + hireDate +
                ", gender=" + gender +
                ", hospital=" + (hospital != null ? hospital.getHospitalName() : "No Hospital Assigned") +
                ", patients=" + medicalRecords.size() + " medical records" +
                '}';
    }

    /**
     * Overriding equals method based on licenseNumber.
     *
     * @param o Object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor)) return false;
        Doctor doctor = (Doctor) o;
        return licenseNumber == doctor.licenseNumber;
    }

    /**
     * Overriding hashCode method based on licenseNumber.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(licenseNumber);
    }
}