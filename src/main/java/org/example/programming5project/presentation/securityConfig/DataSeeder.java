package org.example.programming5project.presentation.securityConfig;

import org.example.programming5project.domain.*;
import org.example.programming5project.repository.DoctorJpaDataRepository;
import org.example.programming5project.repository.PatientJpaDataRepository;
import org.example.programming5project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PatientJpaDataRepository patientRepository;
    private final DoctorJpaDataRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      PatientJpaDataRepository patientRepository,
                      DoctorJpaDataRepository doctorRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {


        // Doctor user
    }
}
