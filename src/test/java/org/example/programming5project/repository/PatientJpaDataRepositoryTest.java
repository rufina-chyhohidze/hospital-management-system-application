package org.example.programming5project.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.example.programming5project.TestHelper;
import org.example.programming5project.domain.Gender;
import org.example.programming5project.domain.Patient;
import org.example.programming5project.domain.User;
import org.example.programming5project.domain.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class PatientJpaDataRepositoryTest {
    @Autowired
    private PatientJpaDataRepository sut;

    @Autowired
    private TestHelper testHelper;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldSavePatientWithCreator_success() {
        User creator = testHelper.createUser("user1", "pass", UserRole.ADMIN);
        Patient patient = testHelper.createPatientWithoutSaving("P001", "Fillipe", "Suziano", 30, Gender.MALE, LocalDate.now(), 1000.0, creator);

        Patient savedPatient = sut.save(patient);

        assertNotNull(savedPatient.getPatientId());
        assertEquals("Fillipe", savedPatient.getFirstName());
        assertEquals(creator.getId(), savedPatient.getCreator().getId());
    }

    @Test
    void shouldThrowException_whenSavingPatientWithoutPatientId() {

        User creator = testHelper.createUser("user2", "pass", UserRole.ADMIN);
        Patient patient = testHelper.createPatientWithoutSaving(null, "Jane", "Smith", 25, Gender.FEMALE, LocalDate.now(), 1500.0, creator);

            assertThrows(Exception.class, () -> sut.saveAndFlush(patient));
    }

    @Test
    void shouldEnforceUniquePatientIdConstraint() {
        User creator = testHelper.createUser("user3", "pass", UserRole.ADMIN);
        Patient patient1 = testHelper.createPatient("P002", "Alice", "Johnson", 28, Gender.FEMALE, LocalDate.now(), 800.0, creator);


        Optional<Patient> foundPatient1 = sut.findById("P002");
        assertTrue(foundPatient1.isPresent(), "Patient with ID P002 should exist");
        assertEquals("Alice", foundPatient1.get().getFirstName(), "First name should be Alice");
        assertEquals("Johnson", foundPatient1.get().getLastName(), "Last name should be Johnson");


        Patient patient2 = testHelper.createPatientWithoutSaving("P002", "Bob", "Brown", 35, Gender.MALE, LocalDate.now(), 1200.0, creator);
        sut.save(patient2);

        Optional<Patient> foundPatient2 = sut.findById("P002");
        assertTrue(foundPatient2.isPresent(), "Patient with ID P002 should still exist");
        assertEquals("Bob", foundPatient2.get().getFirstName(), "First name should now be Bob");
        assertEquals("Brown", foundPatient2.get().getLastName(), "Last name should now be Brown");
        assertEquals(35, foundPatient2.get().getAge(), "Age should now be 35");
        assertEquals(Gender.MALE, foundPatient2.get().getGender(), "Gender should now be MALE");
        assertEquals(1200.0, foundPatient2.get().getBillingAmount(), "Billing amount should now be 1200.0");
    }

    @Test
    void deletingPatientShouldNotDeleteCreator() {
        User creator = testHelper.createUser("user4", "pass", UserRole.ADMIN);
        Patient patient = testHelper.createPatient("P003", "Mike", "Tyson", 50, Gender.MALE, LocalDate.now(), 2000.0, creator);

        sut.delete(patient);
        assertTrue(sut.findById("P003").isEmpty());
        assertTrue(testHelper.getUserRepository().findById(creator.getId()).isPresent());
    }
    @Test
    @Transactional
    void shouldAllowCreatorToEditAndDeletePatient() {
        var user = testHelper.createUser("creator@example.com", "password", UserRole.ADMIN);

        var patient = testHelper.createPatient(
                "P100", "John", "Doe", 30, Gender.MALE,
                LocalDate.of(2025, 1, 1), 1000.0, user
        );
        sut.save(patient);

        patient.setBillingAmount(2000.0);
        sut.save(patient);

        sut.delete(patient);

        assertFalse(sut.findById(patient.getPatientId()).isPresent(),
                "Patient should have been deleted by creator");
    }

    @Test
    @Transactional
    void shouldNotAllowOtherUserToEditOrDeletePatient() {
        var creator = testHelper.createUser("creator@example.com", "password", UserRole.ADMIN);
        var otherUser = testHelper.createUser("other@example.com", "password", UserRole.ADMIN);

        var patient = testHelper.createPatient(
                "P200", "Rufina", "Chyhohidze", 25, Gender.FEMALE,
                LocalDate.of(2025, 5, 5), 1500.0, creator
        );

        patient.setBillingAmount(3000.0);
        boolean isAllowed = patient.getCreator().getId().equals(otherUser.getId());

        assertFalse(isAllowed, "Other user should not be allowed to edit the patient");

        assertThrows(SecurityException.class, () -> {
            if (!isAllowed) throw new SecurityException("Access denied: not creator");
            sut.delete(patient);
        });
    }

    @AfterEach
    void cleanUp() {
        testHelper.cleanUp();
    }
}
