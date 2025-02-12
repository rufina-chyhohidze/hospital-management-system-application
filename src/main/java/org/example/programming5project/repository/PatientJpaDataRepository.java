package org.example.programming5project.repository;

import org.example.programming5project.domain.Patient;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * uses JpaRepositories with additional query methods.
 */
@Profile("jpa")
@Repository
public interface PatientJpaDataRepository  extends JpaRepository<Patient, String> {
    @Query("""
    SELECT p FROM Patient p
    LEFT JOIN FETCH p.medicalRecords mr
    LEFT JOIN FETCH mr.doctor
    WHERE p.patientId = :patientId
    """)
    Optional<Patient> findPatientWithMedicalRecords(@Param("patientId") String patientId);

    @Query("""
    SELECT DISTINCT p FROM Patient p
    LEFT JOIN FETCH p.medicalRecords mr
    WHERE mr.doctor.licenseNumber = :doctorId
    """)
    List<Patient> findPatientsForDoctor(@Param("doctorId") int doctorId);


    @Query("""
    SELECT p FROM Patient p
    WHERE
        LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
        OR p.admissionDate = :admissionDate
    """)
    List<Patient> findByNameOrAdmissionDate(
            @Param("name") String name,
            @Param("admissionDate") LocalDate admissionDate
    );


}


