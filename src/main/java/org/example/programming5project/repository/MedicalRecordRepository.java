package org.example.programming5project.repository;

import org.example.programming5project.domain.MedicalRecord;
import org.example.programming5project.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    void deleteByPatient(Patient patient);


}
