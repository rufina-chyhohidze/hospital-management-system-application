package org.example.programming5project.repository;

import org.example.programming5project.domain.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * uses JpaRepositories with additional query methods.
 */
@Repository
public interface DoctorJpaDataRepository extends JpaRepository<Doctor, Integer> {
    @Query("""
    SELECT d FROM Doctor d
    LEFT JOIN FETCH d.medicalRecords mr
    LEFT JOIN FETCH mr.patient
    WHERE mr.patient.patientId = :patientId
    """)
    List<Doctor> findDoctorsForPatient(@Param("patientId") String patientId);

    @Query("""
SELECT DISTINCT d FROM Doctor d
LEFT JOIN FETCH d.hospital
LEFT JOIN FETCH d.medicalRecords mr
LEFT JOIN FETCH mr.patient p
LEFT JOIN FETCH p.creator
WHERE d.licenseNumber = :licenseNumber
""")
    Optional<Doctor> findDoctorByLicenseNumber(@Param("licenseNumber") int licenseNumber);
}
