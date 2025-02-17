package org.example.programming5project.presentation.api;

import org.example.programming5project.service.HospitalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalRestController {
    private final HospitalService hospitalService;
    private final Logger logger = LoggerFactory.getLogger(HospitalRestController.class);

    public HospitalRestController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @DeleteMapping("/{hospitalId}")
    public ResponseEntity<Void> deleteHospital(@PathVariable Long hospitalId) {
        logger.info("Attempting to delete hospital with ID: {}", hospitalId);

        boolean deleted = hospitalService.deleteHospital(hospitalId);

        if (!deleted) {
            logger.warn("Hospital with ID {} not found.", hospitalId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        logger.info("Hospital with ID {} deleted successfully.", hospitalId);
        return ResponseEntity.noContent().build();
    }
}
