package org.example.programming5project.presentation.mvc.viewmodels;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

public class HospitalForm {
    @NotBlank(message = "Hospital name is required")
    private String hospitalName;

    @NotBlank(message = "Address is required")
    private String hospitalAddress;

    @NotNull(message = "Established date is required")
    @PastOrPresent(message = "Established date cannot be in the future")
    private LocalDate establishedDate;
    @NotEmpty(message = "At least one department is required")
    private List<String> departments;

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public LocalDate getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(LocalDate establishedDate) {
        this.establishedDate = establishedDate;
    }

    public List<String> getDepartments() {
        return departments;
    }

    public void setDepartments(List<String> departments) {
        this.departments = departments;
    }

}
