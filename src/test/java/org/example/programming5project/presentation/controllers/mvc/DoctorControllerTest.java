package org.example.programming5project.presentation.controllers.mvc;

import org.example.programming5project.TestHelper;
import org.example.programming5project.domain.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestHelper testHelper;

    private Hospital hospital;

    @BeforeEach
    void setUp() {
        testHelper.createUser("admin@kdg.be", "admin123", UserRole.ADMIN);
        testHelper.createUser("user@kdg.be", "doc123", UserRole.USER);
        hospital = testHelper.createHospital("Antwerp Hospital", "Anterpstraat 12");
    }

    @Test
    @WithUserDetails(value = "admin@kdg.be", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAddDoctorIfValidAndAdmin() throws Exception {
        mockMvc.perform(post("/doctors/add")
                        .param("firstName", "Eva")
                        .param("lastName", "Reichel")
                        .param("licenseNumber", "12345")
                        .param("salary", "75000")
                        .param("department", "CARDIOLOGY")
                        .param("hireDate", "2023-01-01")
                        .param("gender", "FEMALE")
                        .param("hospitalId", String.valueOf(hospital.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors"));
    }

    @Test
    @WithUserDetails(value = "admin@kdg.be", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldReturnFormWithErrorsIfInvalidDoctorForm() throws Exception {
        mockMvc.perform(post("/doctors/add")
                        .param("firstName", "")
                        .param("lastName", "Doe")
                        .param("licenseNumber", "abc")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("adddoctor"))
                .andExpect(model().attributeHasErrors("doctorForm"));
    }

    @Test
    @WithUserDetails(value = "admin@kdg.be", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldDeleteDoctorIfAdmin() throws Exception {
        Doctor doctor = testHelper.createDoctor("John", "Smith", Department.NEUROLOGY, 9999, 50000, LocalDate.now(), Gender.MALE, hospital);

        mockMvc.perform(post("/doctors/delete/{licenseNumber}", doctor.getLicenseNumber())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctors"));
    }

    @Test
    void shouldRedirectToLoginIfNotSignedInOnAddDoctor() throws Exception {
        mockMvc.perform(post("/doctors/add")
                        .param("firstName", "Jane")
                        .param("lastName", "Doe")
                        .param("licenseNumber", "12345")
                        .param("salary", "75000")
                        .param("department", "CARDIOLOGY")
                        .param("hireDate", "2023-01-01")
                        .param("gender", "FEMALE")
                        .param("hospitalId", String.valueOf(hospital.getId()))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithUserDetails(value = "user@kdg.be", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldShowDoctorListPage() throws Exception {
        mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctors"))
                .andExpect(model().attributeExists("doctors"));
    }

    @Test
    @WithUserDetails(value = "admin@kdg.be", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldShowDoctorDetailsPageIfValidId() throws Exception {
        Doctor doctor = testHelper.createDoctor("Anna", "Taylor", Department.CARDIOLOGY, 7777, 62000, LocalDate.now(), Gender.FEMALE, hospital);

        mockMvc.perform(get("/doctors/{doctorId}", doctor.getLicenseNumber()))
                .andExpect(status().isOk())
                .andExpect(view().name("doctorDetails"))
                .andExpect(model().attributeExists("doctor", "assignedPatients", "allPatients", "medicalRecords"));
    }

    @Test
    @WithUserDetails(value = "admin@kdg.be", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldReturnErrorPageIfDoctorNotFound() throws Exception {
        mockMvc.perform(get("/doctors/999999"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @AfterEach
    void cleanUp() {
        testHelper.cleanUp();
    }
}