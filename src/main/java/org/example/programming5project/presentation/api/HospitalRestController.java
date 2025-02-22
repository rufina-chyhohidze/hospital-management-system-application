package org.example.programming5project.presentation.api;

import org.example.programming5project.presentation.api.dtos.HospitalDto;
import org.example.programming5project.service.HospitalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalRestController {
    private final HospitalService hospitalService;
    private final Logger logger = LoggerFactory.getLogger(HospitalRestController.class);

    public HospitalRestController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @GetMapping
    public ResponseEntity<List<HospitalDto>> searchHospitals(@RequestParam(required = false, defaultValue = "") String search) {
        if (search.trim().isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        List<HospitalDto> searchResult = hospitalService.searchHospitals(search);

        if (searchResult.isEmpty()) {
            logger.info("No hospital found with name: {}", search);
            return ResponseEntity.noContent().build();
        } else {
            logger.info("Hospital found with name: {}", search);
            return ResponseEntity.ok(searchResult);
        }
    }
}
