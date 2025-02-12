package org.example.programming5project.presentation.api;

import org.example.programming5project.presentation.api.dtos.PatientDto;
import org.example.programming5project.service.PatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/patients")
public class PatientRestController {
    private static final Logger logger = LoggerFactory.getLogger(PatientRestController.class);
    private final PatientService patientService;

    public PatientRestController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> patientDtos = patientService.getAllPatients()
                .stream()
                .map(PatientDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(patientDtos);
    }
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deletePatient(@PathVariable String patientId) {
        var patientOptional = Optional.ofNullable(patientService.findPatientById(patientId));

        if (patientOptional.isEmpty()) {
            logger.warn("Patient with ID {} not found.", patientId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }

        patientService.removePatient(patientId);
        logger.info("Patient with ID {} deleted.", patientId);
        return ResponseEntity.noContent().build(); // 204
    }
}
