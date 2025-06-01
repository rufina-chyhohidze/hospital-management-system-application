package org.example.programming5project.repository;

import org.example.programming5project.domain.Patient;
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
@Repository
public interface PatientJpaDataRepository  extends JpaRepository<Patient, String> {
    @Query("""
SELECT DISTINCT p FROM Patient p
LEFT JOIN FETCH p.creator
LEFT JOIN FETCH p.medicalRecords mr
LEFT JOIN FETCH mr.doctor d
LEFT JOIN FETCH d.hospital
WHERE p.patientId = :patientId
""")
    Optional<Patient> findPatientWithMedicalRecords(@Param("patientId") String patientId);

    @Query("""
    SELECT DISTINCT p FROM Patient p
    LEFT JOIN FETCH p.creator
    LEFT JOIN FETCH p.medicalRecords mr
    LEFT JOIN FETCH mr.doctor
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

    @Query("SELECT p FROM Patient p WHERE p.creator.id = :userId")
    List<Patient> findByCreatorId(@Param("userId") Long userId);

    @Query("SELECT p FROM Patient p LEFT JOIN FETCH p.creator WHERE p.patientId = :patientId")
    Optional<Patient> findByIdWithCreator(@Param("patientId") String patientId);

}


