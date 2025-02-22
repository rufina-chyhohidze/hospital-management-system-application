package org.example.programming5project.presentation.api;

import jakarta.validation.Valid;
import org.example.programming5project.domain.Patient;
import org.example.programming5project.exceptions.PatientNotFoundException;
import org.example.programming5project.presentation.api.dtos.AddPatientDto;
import org.example.programming5project.presentation.api.dtos.PatientDto;
import org.example.programming5project.presentation.api.dtos.PatientMapper;
import org.example.programming5project.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    public PatientRestController(PatientService patientService, PatientMapper patientMapper) {
        this.patientService = patientService;
        this.patientMapper = patientMapper;
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
    public ResponseEntity<Void> deletePatient(@PathVariable String patientId) {
        try {
            patientService.findPatientById(patientId);
        } catch (PatientNotFoundException ex) {
            logger.warn("Patient with ID {} not found.", patientId);
            return ResponseEntity.notFound().build();//404
        }

        patientService.removePatient(patientId);
        logger.info("Patient with ID {} deleted.", patientId);
        return ResponseEntity.noContent().build();//204
    }


    @PostMapping
    public ResponseEntity<PatientDto> addPatient(@Valid @RequestBody AddPatientDto addPatientDto) {
        Patient patient = patientMapper.toEntity(addPatientDto);

        if (patient.getPatientId() == null || patient.getPatientId().isBlank()) {
            patient.setPatientId(UUID.randomUUID().toString());
        }

        patientService.addPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(patientMapper.toDto(patient));
    }


}
