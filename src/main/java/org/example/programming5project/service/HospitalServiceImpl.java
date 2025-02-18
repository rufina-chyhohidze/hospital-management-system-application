package org.example.programming5project.service;

import org.example.programming5project.domain.Department;
import org.example.programming5project.domain.Doctor;
import org.example.programming5project.domain.Hospital;
import org.example.programming5project.presentation.api.dtos.HospitalDto;
import org.example.programming5project.presentation.mvc.viewmodels.HospitalForm;
import org.example.programming5project.repository.DoctorJpaDataRepository;
import org.example.programming5project.repository.HospitalJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class HospitalServiceImpl implements HospitalService {
    private final HospitalJpaRepository hospitalJpaRepository;
    private final DoctorJpaDataRepository doctorJpaDataRepository;
    private final Logger logger = LoggerFactory.getLogger(HospitalServiceImpl.class);

    public HospitalServiceImpl(HospitalJpaRepository hospitalJpaRepository, DoctorJpaDataRepository doctorJpaDataRepository) {
        this.hospitalJpaRepository = hospitalJpaRepository;
        this.doctorJpaDataRepository = doctorJpaDataRepository;
    }
    @Override
    public List<HospitalDto> getAllHospitals() {
        logger.debug("getting all hospitals...");
        return hospitalJpaRepository.findAllWithDepartments()
                .stream()
                .map(hospital -> new HospitalDto(
                        hospital.getId(),
                        hospital.getHospitalName(),
                        hospital.getHospitalAddress(),
                        hospital.getEstablishedDate(),
                        hospital.getDepartments()  //
                ))
                .toList();
    }

    @Override
    public Hospital findById(Long id) {
        return hospitalJpaRepository.findById(id).orElseThrow(() -> new RuntimeException("Hospital not found"));
    }

    @Override
    public void addHospital(HospitalForm hospitalForm) {
        Hospital hospital = new Hospital();
        hospital.setHospitalName(hospitalForm.getHospitalName());
        hospital.setHospitalAddress(hospitalForm.getHospitalAddress());
        hospital.setEstablishedDate(hospitalForm.getEstablishedDate());
        hospital.setDepartments(hospitalForm.getDepartments().stream()
                .map(Department::valueOf)
                .collect(Collectors.toList()));

        hospitalJpaRepository.save(hospital);
    }

  //  @Override
  //  @Transactional
  //  public boolean deleteHospital(Long hospitalId) {
  //      Optional<Hospital> hospitalOptional = hospitalJpaRepository.findById(hospitalId);
//
  //      if (hospitalOptional.isEmpty()) {
  //          return false;
  //      }
//
  //      Hospital hospital = hospitalOptional.get();
//
  //      List<Doctor> doctors = doctorJpaDataRepository.findByHospital(hospital);
  //      for (Doctor doctor : doctors) {
  //          doctor.setHospital(null);
  //      }
  //      doctorJpaDataRepository.saveAll(doctors);
//
  //      hospitalJpaRepository.deleteById(hospitalId);
  //      return true;
  //  }

    @Override
    public List<HospitalDto> searchHospitals(String search) {
        List<Hospital> hospitals = hospitalJpaRepository
                .findByHospitalNameContainingIgnoreCaseOrHospitalAddressContainingIgnoreCase(search, search);

        return hospitals.stream()
                .map(h -> new HospitalDto(h.getId(), h.getHospitalName(), h.getHospitalAddress(), h.getEstablishedDate(), h.getDepartments()))
                .toList();
    }

}
