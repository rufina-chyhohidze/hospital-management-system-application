package org.example.programming5project.presentation.api.dtos;

import org.example.programming5project.domain.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    @Mapping(target = "patientId", ignore = true) // Ignore ID since it's auto-generated
    Patient toEntity(AddPatientDto addPatientDto);

    PatientDto toDto(Patient patient);
}
