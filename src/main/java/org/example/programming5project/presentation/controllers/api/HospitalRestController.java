package org.example.programming5project.presentation.controllers.api;

import org.example.programming5project.presentation.controllers.api.dtos.HospitalDto;
import org.example.programming5project.presentation.controllers.api.dtos.HospitalMapper;
import org.example.programming5project.presentation.controllers.api.dtos.UpdateHospitalDto;
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
    private final HospitalMapper hospitalMapper;
    private final Logger logger = LoggerFactory.getLogger(HospitalRestController.class);

    public HospitalRestController(HospitalService hospitalService,HospitalMapper hospitalMapper) {
        this.hospitalService = hospitalService;
        this.hospitalMapper = hospitalMapper;
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

    @GetMapping("/{id}")
    public ResponseEntity<HospitalDto> getOneHospital(@PathVariable Long id) {
        System.out.println("Attempting to fetch hospital with ID: " + id);
        return hospitalService.findHospitalById(id)
                .map(hospitalMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateHospital(@PathVariable Long id, @RequestBody UpdateHospitalDto dto) {
        boolean updated = hospitalService.updateHospital(id, dto);
        if (updated) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
