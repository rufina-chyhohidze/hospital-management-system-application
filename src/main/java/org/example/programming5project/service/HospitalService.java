package org.example.programming5project.service;

import org.example.programming5project.domain.Hospital;
import org.example.programming5project.presentation.api.dtos.HospitalDto;
import org.example.programming5project.presentation.mvc.viewmodels.HospitalForm;

import java.util.List;

public interface HospitalService {
    List<HospitalDto> getAllHospitals();
    Hospital findById(Long id);
    void addHospital(HospitalForm hospitalForm);
}
