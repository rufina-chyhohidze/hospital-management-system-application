package org.example.programming5project.presentation.controllers.api;

import jakarta.validation.Valid;
import org.example.programming5project.domain.Patient;
import org.example.programming5project.domain.User;
import org.example.programming5project.exceptions.PatientNotFoundException;
import org.example.programming5project.presentation.controllers.api.dtos.AddPatientDto;
import org.example.programming5project.presentation.controllers.api.dtos.PatientDto;
import org.example.programming5project.presentation.controllers.api.dtos.PatientMapper;
import org.example.programming5project.presentation.controllers.api.dtos.UpdatePatientDto;
import org.example.programming5project.service.PatientService;
import org.example.programming5project.service.UserService;
import org.example.programming5project.service.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PatientRestController {
    private static final Logger logger = LoggerFactory.getLogger(PatientRestController.class);
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final UserService userService;


    public PatientRestController(PatientService patientService, PatientMapper patientMapper, UserService userService) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> patientDtos = patientService.getAllPatients()
                .stream()
                .map(PatientDto::fromEntity)
                .collect(Collectors.toList());
        logger.info("Found {} patients", patientDtos.size());
        return ResponseEntity.ok(patientDtos);
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<?> deletePatient(@PathVariable String patientId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<Patient> patientOptional = patientService.findPatientByIdWithCreator(patientId);

        if (patientOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Patient patient = patientOptional.get();

        if (!patient.getCreator().getId().equals(userDetails.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("You can only delete your own patients"); //403
        }

        logger.info("Deleting patient with ID: {}", patientId);
        patientService.removePatient(patientId);
        logger.info("Patient deleted successfully: {}", patientId);
        return ResponseEntity.noContent().build();//204
    }


    @PostMapping
    public ResponseEntity<PatientDto> addPatient(@Valid @RequestBody AddPatientDto addPatientDto,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User creator = userService.findById(userDetails.getUserId());
        Patient patient = patientMapper.toEntity(addPatientDto);
        if (patient.getPatientId() == null || patient.getPatientId().isBlank()) {
            patient.setPatientId(UUID.randomUUID().toString());
        }
        logger.info("Adding patient: {}", patient.toString());
        patient.setCreator(creator);
        patientService.addPatient(patient);
        logger.info("Patient added successfully: {}", patient.toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(patientMapper.toDto(patient));//201
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePatient(
            @PathVariable String id,
            @RequestBody @Valid UpdatePatientDto updatePatientDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        logger.info("Updating patient with ID: {}", id);
        Patient patient = patientService.findPatientByIdWithCreator(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        if (!patient.getCreator().getId().equals(userDetails.getUserId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        logger.info("Patient details for ID updated: {}", id);

        boolean isUpdated = patientService.updatePatientDetails(id,
                updatePatientDto.billingAmount(), updatePatientDto.admissionDate());

        return isUpdated ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
