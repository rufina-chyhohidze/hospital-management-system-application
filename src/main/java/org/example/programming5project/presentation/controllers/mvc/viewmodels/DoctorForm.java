package org.example.programming5project.presentation.controllers.mvc.viewmodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class DoctorForm {
    private static final Logger logger = LoggerFactory.getLogger(DoctorForm.class);
    @NotBlank(message = "First name is required")
    private String firstName;
    @Size(min=2, max=30)
    @NotBlank(message = "Last name is required")
    private String lastName;
    @NotNull
    private int licenseNumber;
    @NotNull
    private double salary;
    @NotNull
    private String department;
    @NotNull
    private LocalDate hireDate;
    @NotNull
    private String gender;
    @NotNull(message = "Hospital is required")
    private Long hospitalId;

    public DoctorForm() {
        logger.debug("Inside DoctorForm...");
    }

    public @NotBlank(message = "First name is required") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First name is required") String firstName) {
        this.firstName = firstName;
    }

    public @Size(min = 2, max = 30) @NotBlank(message = "Last name is required") String getLastName() {
        return lastName;
    }

    public void setLastName(@Size(min = 2, max = 30) @NotBlank(message = "Last name is required") String lastName) {
        this.lastName = lastName;
    }

    @NotNull
    public int getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(@NotNull int licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @NotNull
    public double getSalary() {
        return salary;
    }

    public void setSalary(@NotNull double salary) {
        this.salary = salary;
    }

    public @NotNull String getDepartment() {
        return department;
    }

    public void setDepartment(@NotNull String department) {
        this.department = department;
    }

    public @NotNull LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(@NotNull LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public @NotNull String getGender() {
        return gender;
    }

    public void setGender(@NotNull String gender) {
        this.gender = gender;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    @Override
    public String toString() {
        return "Doctor's data:{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", licenseNumber=" + licenseNumber +
                ", salary=" + salary +
                ", department='" + department + '\'' +
                ", hireDate=" + hireDate +
                ", gender='" + gender + '\'' +
                '}';
    }
}
