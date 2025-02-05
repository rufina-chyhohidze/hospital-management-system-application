package org.example.programming5project.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Hospital (One-to-Many with Doctor)
 * A Hospital can employ many Doctors. Each Doctor works in only one Hospital.
 */
@Entity
public class Hospital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String hospitalName;
    private String hospitalAddress;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<Department> departments = new ArrayList<>(); // Changed from String[] to List<Department>
    private LocalDate establishedDate;

    // One-to-Many relationship: Hospital has many Doctors
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Doctor> doctors = new ArrayList<>();

    public Hospital(String hospitalName, String hospitalAddress, List<Department> departments, LocalDate establishedDate) {
        if (establishedDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Established date cannot be in the future.");
        }
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.departments = new ArrayList<>(departments); // Defensive copy
        this.establishedDate = establishedDate;
        this.doctors = new ArrayList<>();
    }

    public Hospital(){

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public void setEstablishedDate(LocalDate establishedDate) {
        this.establishedDate = establishedDate;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    // Getters
    public String getHospitalName() {
        return hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public List<Department> getDepartments() {
        return new ArrayList<>(departments); // Return a copy to maintain encapsulation
    }

    public LocalDate getEstablishedDate() {
        return establishedDate;
    }

    public List<Doctor> getDoctors() {
        return new ArrayList<>(doctors); // Return a copy to maintain encapsulation
    }

    /**
     * Adds a doctor to the hospital and sets the hospital reference in the doctor.
     *
     * @param doctor The Doctor to be added
     */
    public void addDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null.");
        }
        if (!doctors.contains(doctor)) {
            doctors.add(doctor);
            doctor.setHospital(this); // Establish bidirectional relationship
        }
    }

    /**
     * Removes a doctor from the hospital and unsets the hospital reference in the doctor.
     *
     * @param doctor The Doctor to be removed
     */
    public void removeDoctor(Doctor doctor) {
        if (doctors.contains(doctor)) {
            doctors.remove(doctor);
            doctor.setHospital(null); // Remove bidirectional relationship
        }
    }

    /**
     * Calculates the age of the hospital in years.
     *
     * @return Age in years
     */
    public int getHospitalAge() {
        return Period.between(establishedDate, LocalDate.now()).getYears();
    }

    /**
     * toString method for Hospital class.
     *
     * @return String representation of Hospital
     */
    @Override
    public String toString() {
        StringBuilder deptBuilder = new StringBuilder();
        for (int i = 0; i < departments.size(); i++) {
            deptBuilder.append(departments.get(i).name());
            if (i < departments.size() - 1) {
                deptBuilder.append(", ");
            }
        }

        return "Hospital{" +
                "hospitalName='" + hospitalName + '\'' +
                ", hospitalAddress='" + hospitalAddress + '\'' +
                ", departments=" + deptBuilder.toString() +
                ", establishedDate=" + establishedDate +
                ", age=" + getHospitalAge() + " years" +
                ", numberOfDoctors=" + doctors.size() + " doctors" +
                '}';
    }

    /**
     * Overriding equals method based on hospitalName and hospitalAddress.
     *
     * @param o Object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hospital)) return false;
        Hospital hospital = (Hospital) o;
        return Objects.equals(hospitalName, hospital.hospitalName) &&
                Objects.equals(hospitalAddress, hospital.hospitalAddress);
    }

    /**
     * Overriding hashCode method based on hospitalName and hospitalAddress.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(hospitalName, hospitalAddress);
    }
}