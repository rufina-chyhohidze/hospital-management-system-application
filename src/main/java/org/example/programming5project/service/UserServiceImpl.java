package org.example.programming5project.service;

import org.example.programming5project.domain.User;
import org.example.programming5project.domain.UserRole;
import org.example.programming5project.presentation.mvc.mvcdto.UserDto;
import org.example.programming5project.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserDto userDto) {
        if (userRepository.findByUsername(userDto.username()).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }

        User user = new User();
        user.setUsername(userDto.username());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setUserRole(UserRole.ADMIN); // default role
        userRepository.save(user);
    }
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }
}
