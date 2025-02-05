package org.example.programming5project.repository;

import org.example.programming5project.domain.Patient;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * uses JpaRepositories with additional query methods.
 */
@Profile("jpa")
@Repository
public interface PatientJpaDataRepository  extends JpaRepository<Patient, String> {
    @Query("SELECT p FROM Patient p WHERE p.firstName = :name OR p.admissionDate = :admissionDate")
    List<Patient> findByNameOrAdmissionDate(@Param("name") String name, @Param("admissionDate") LocalDate admissionDate);

    @Query("SELECT p FROM Patient p JOIN p.doctors d WHERE d.licenseNumber = :doctorId")
    List<Patient> findPatientsForDoctor(@Param("doctorId") int doctorId);

}
