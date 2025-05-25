package org.example.programming5project.service;

import org.example.programming5project.domain.Hospital;
import org.example.programming5project.presentation.controllers.api.dtos.HospitalDto;
import org.example.programming5project.presentation.controllers.api.dtos.UpdateHospitalDto;
import org.example.programming5project.presentation.controllers.mvc.viewmodels.HospitalForm;

import java.util.List;
import java.util.Optional;

public interface HospitalService {
    List<HospitalDto> getAllHospitals();
    Hospital findById(Long id);
    void addHospital(HospitalForm hospitalForm);
    List<HospitalDto> searchHospitals(String search);
    Optional<Hospital> findHospitalById(Long id);
    boolean updateHospital(Long id, UpdateHospitalDto dto);

}
