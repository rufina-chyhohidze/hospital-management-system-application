package org.example.programming5project.service;

import org.example.programming5project.domain.User;
import org.example.programming5project.presentation.mvc.mvcdto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService  {
    void registerUser(UserDto userDto);
    User findById(Long id);
}

