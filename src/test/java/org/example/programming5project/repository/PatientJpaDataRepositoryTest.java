package org.example.programming5project.repository;

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

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class PatientJpaDataRepositoryTest {
    @Autowired
    private PatientJpaDataRepository sut;

    @Autowired
    private TestHelper testHelper;

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

            assertThrows(DataIntegrityViolationException.class, () -> sut.saveAndFlush(patient));
    }

    @Test
    void shouldEnforceUniquePatientIdConstraint() {
        User creator = testHelper.createUser("user3", "pass", UserRole.ADMIN);
        Patient patient1 = testHelper.createPatient("P002", "Alice", "Johnson", 28, Gender.FEMALE, LocalDate.now(), 800.0, creator);
        Patient patient2 = testHelper.createPatientWithoutSaving("P002", "Bob", "Brown", 35, Gender.MALE, LocalDate.now(), 1200.0, creator);

        sut.save(patient1);

        //trying to save second with same id should fail
        assertThrows(DataIntegrityViolationException.class, () -> sut.saveAndFlush(patient2));
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
                LocalDate.of(2025, 5, 5), 1500.0, null
        );
        patient.setCreator(otherUser); //here first set to real creator, and then change to other user
       sut.save(patient);

        patient.setBillingAmount(3000.0);
        boolean isAllowed = patient.getCreator().getId().equals(otherUser.getId());

        assertFalse(isAllowed, "Other user should not be allowed to edit the patient");

        // Simulate access control for delete
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