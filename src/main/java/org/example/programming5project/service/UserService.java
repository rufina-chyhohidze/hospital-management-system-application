package org.example.programming5project.service;

import org.example.programming5project.domain.User;
import org.example.programming5project.presentation.controllers.mvc.mvcdto.UserDto;


public interface UserService  {
    void registerUser(UserDto userDto);
    User findById(Long id);
}

