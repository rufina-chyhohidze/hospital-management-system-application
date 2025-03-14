package org.example.programming5project.service;

import org.example.programming5project.presentation.mvc.mvcdto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    void registerUser(UserDto userDto);
}

