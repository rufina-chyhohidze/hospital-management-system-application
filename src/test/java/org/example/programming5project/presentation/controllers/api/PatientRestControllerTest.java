package org.example.programming5project.presentation.controllers.api;

import org.example.programming5project.TestHelper;
import org.example.programming5project.domain.Gender;
import org.example.programming5project.domain.Patient;
import org.example.programming5project.domain.User;
import org.example.programming5project.domain.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithUserDetails;


import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PatientRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestHelper testHelper;

    private User admin;
    private User otherAdmin;
    private User doctor;
    private Patient patient;

    @BeforeEach
    void setUp() {
        admin = testHelper.createUser("admin@med.com", "admin123", UserRole.ADMIN);
        otherAdmin = testHelper.createUser("otheradmin@med.com", "admin456", UserRole.ADMIN);
        doctor = testHelper.createUser("doctor@med.com", "doc123", UserRole.USER);
        patient = testHelper.createPatient(
                "p123", "Anna", "Smith", 40, Gender.FEMALE,
                LocalDate.of(2025, 5, 1), 250.0, admin
        );
    }


    @Test
    @WithUserDetails(value = "admin@med.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAllowAdminToDeleteOwnPatient() throws Exception {
        mockMvc.perform(delete("/api/patients/{id}", patient.getPatientId())
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
    @Test
    @WithUserDetails(value = "otheradmin@med.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldForbidAdminFromDeletingOthersPatient() throws Exception {
        mockMvc.perform(delete("/api/patients/{id}", patient.getPatientId())
                        .with(csrf()))
                .andExpect(status().isForbidden())
                .andExpect(content().string(containsString("You can only delete your own patients")));
    }

    @Test
    @WithUserDetails(value = "doctor@med.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldForbidUserFomDeletingPatient() throws Exception {
        mockMvc.perform(delete("/api/patients/{id}", patient.getPatientId())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "admin@med.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAllowAdminToUpdateOwnPatient() throws Exception {
        mockMvc.perform(patch("/api/patients/{id}", patient.getPatientId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "billingAmount": 300.0,
                                  "admissionDate": "2025-06-01"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails(value = "otheradmin@med.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldForbidAdminFromUpdatingOthersPatient() throws Exception {
        mockMvc.perform(patch("/api/patients/{id}", patient.getPatientId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "billingAmount": 300.0,
                                  "admissionDate": "2025-04-01"
                                }
                                """)
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
    @AfterEach
    void cleanUp() {
        testHelper.cleanUp();
    }

}