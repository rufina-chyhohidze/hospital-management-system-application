package org.example.programming5project.presentation.controllers.mvc.viewmodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class PatientForm {
    private static final Logger logger = LoggerFactory.getLogger(PatientForm.class);
    @NotBlank(message = "First name is required")
    @Size(min=2, max=30)
    private String firstName;

    @Size(min=2, max=30)
    @NotBlank(message = "Last name is required")
    private String lastName;

    private String patientId;
    @NotNull
    private int age;
    @NotBlank(message = "Gender is required")
    private String gender;
    @NotNull
    private LocalDate admissionDate;
    @NotNull
    private double billingAmount;
    private Integer doctorId;
    // Getters and Setters

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }


    public PatientForm() {
        logger.debug("Inside PatientForm...");
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    @NotNull
    public int getAge() {
        return age;
    }

    public void setAge(@NotNull int age) {
        this.age = age;
    }

    public @NotNull String getGender() {
        return gender;
    }

    public void setGender(@NotNull String gender) {
        this.gender = gender;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public double getBillingAmount() {
        return billingAmount;
    }

    public void setBillingAmount(double billingAmount) {
        this.billingAmount = billingAmount;
    }

    @Override
    public String toString() {
        return "Patient's data: {" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patientId='" + patientId + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", admissionDate=" + admissionDate +
                ", billingAmount=" + billingAmount +
                '}';
    }
}
