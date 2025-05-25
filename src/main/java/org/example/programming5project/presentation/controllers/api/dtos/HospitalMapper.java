package org.example.programming5project.presentation.controllers.api.dtos;

import org.example.programming5project.domain.Hospital;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HospitalMapper {
    HospitalDto toDto(Hospital hospital);
}
