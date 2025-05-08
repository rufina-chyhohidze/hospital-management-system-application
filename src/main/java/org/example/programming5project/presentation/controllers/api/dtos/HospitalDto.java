package org.example.programming5project.presentation.controllers.api.dtos;

import org.example.programming5project.domain.Department;

import java.time.LocalDate;
import java.util.List;

public record HospitalDto(Long id,
                          String hospitalName,
                          String hospitalAddress,
                          LocalDate establishedDate,
                          List<Department> departments)
{



}
