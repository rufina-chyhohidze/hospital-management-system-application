package org.example.programming5project.presentation.controllers.api.dtos;

import org.example.programming5project.domain.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(target = "patientId", ignore = true)
    Patient toEntity(AddPatientDto addPatientDto);

    @Mapping(source = "creator.id", target = "creatorId")
    PatientDto toDto(Patient patient);
}
