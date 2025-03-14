package org.example.programming5project.presentation.securityConfig;

import org.example.programming5project.domain.User;
import org.example.programming5project.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("doctor1").isEmpty()) {
            User user = new User();
            user.setUsername("doctor1");
            user.setPassword(passwordEncoder.encode("password123"));
            user.setRole("DOCTOR");
            userRepository.save(user);
        }
    }
}
