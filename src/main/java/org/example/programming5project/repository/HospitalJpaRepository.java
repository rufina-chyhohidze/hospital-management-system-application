package org.example.programming5project.repository;

import org.example.programming5project.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalJpaRepository  extends JpaRepository<Hospital,Long> {
    @Query("""
        SELECT h FROM Hospital h
        LEFT JOIN FETCH h.departments
    """)
    List<Hospital> findAllWithDepartments();

}
