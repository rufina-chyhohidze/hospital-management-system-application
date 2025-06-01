package org.example.programming5project.presentation.controllers.api;

import org.example.programming5project.domain.Patient;
import org.example.programming5project.domain.User;
import org.example.programming5project.presentation.controllers.api.dtos.PatientMapper;
import org.example.programming5project.presentation.controllers.api.dtos.UpdatePatientDto;
import org.example.programming5project.service.PatientService;
import org.example.programming5project.service.UserService;
import org.example.programming5project.service.security.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class PatientRestControllerUnitTest {

    @Autowired
    private PatientRestController sut;

    @MockitoBean
    private PatientService patientService;

    @MockitoBean
    private PatientMapper patientMapper;

    @MockitoBean
    private UserService userService;
    @Test
    void deleteShouldReturnForbiddenIfUserIsNotOwner() {
        // Arrange
        var patient = new Patient();
        var creator = new User();
        creator.setId(100L);
        patient.setCreator(creator);
        patient.setPatientId("p123");

        var userDetails = mock(UserDetailsImpl.class);
        given(userDetails.getUserId()).willReturn(200L);

        given(patientService.findPatientByIdWithCreator("p123")).willReturn(Optional.of(patient));

        // Act
        var response = sut.deletePatient("p123", userDetails);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("You can only delete your own patients"));
        verify(patientService, never()).removePatient(any());
    }

    @Test
    void deleteShouldSucceedIfUserIsOwner() {
        // Arrange
        var patient = new Patient();
        var creator = new User();
        creator.setId(123L);
        patient.setCreator(creator);
        patient.setPatientId("p123");

        var userDetails = mock(UserDetailsImpl.class);
        given(userDetails.getUserId()).willReturn(123L);

        given(patientService.findPatientByIdWithCreator("p123")).willReturn(Optional.of(patient));

        // Act
        var response = sut.deletePatient("p123", userDetails);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(patientService).removePatient("p123");
    }


    @Test
    void updateShouldReturnForbiddenIfUserIsNotOwner() {
        // Arrange
        var patient = new Patient();
        var creator = new User();
        creator.setId(100L);
        patient.setCreator(creator);
        patient.setPatientId("p123");

        var userDetails = mock(UserDetailsImpl.class);
        given(userDetails.getUserId()).willReturn(200L);

        var updateDto = new UpdatePatientDto(500.0, LocalDate.of(2025, 5, 30));

        given(patientService.findPatientByIdWithCreator("p123"))
                .willReturn(Optional.of(patient));

        // Act
        var response = sut.updatePatient("p123", updateDto, userDetails);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        verify(patientService, never()).updatePatientDetails(anyString(), anyDouble(), any(LocalDate.class));
    }

    @Test
    void updateShouldReturnNoContentIfUpdateSucceeds() {
        // Arrange
        var patient = new Patient();
        var creator = new User();
        creator.setId(123L);
        patient.setCreator(creator);
        patient.setPatientId("p123");

        var userDetails = mock(UserDetailsImpl.class);
        given(userDetails.getUserId()).willReturn(123L);

        var updateDto = new UpdatePatientDto(300.0, LocalDate.of(2025, 7, 1));

        given(patientService.findPatientByIdWithCreator("p123")).willReturn(Optional.of(patient));

        given(patientService.updatePatientDetails("p123", 300.0, LocalDate.of(2025, 7, 1))).willReturn(true);

        // Act
        var response = sut.updatePatient("p123", updateDto, userDetails);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(patientService).updatePatientDetails("p123", 300.0, LocalDate.of(2025, 7, 1));
    }

    @Test
    void updateShouldReturnNotFoundIfServiceReturnsFalse() {
        // Arrange
        var patient = new Patient();
        var creator = new User();
        creator.setId(123L);
        patient.setCreator(creator);
        patient.setPatientId("p123");

        var userDetails = mock(UserDetailsImpl.class);
        given(userDetails.getUserId()).willReturn(123L);

        var updateDto = new UpdatePatientDto(250.0, LocalDate.of(2025, 6, 1));

        given(patientService.findPatientByIdWithCreator("p123")).willReturn(Optional.of(patient));

        given(patientService.updatePatientDetails("p123", 250.0, LocalDate.of(2025, 6, 1))).willReturn(false);

        // Act
        var response = sut.updatePatient("p123", updateDto, userDetails);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}