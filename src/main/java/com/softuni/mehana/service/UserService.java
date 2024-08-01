package com.softuni.mehana.service;

import com.softuni.mehana.model.dto.UserRegisterDto;
import com.softuni.mehana.model.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {
    void registerUser(UserRegisterDto registerDTO);

    UserEntity getCurrentUser(UserDetails userDetails);

    boolean comparePasswords(UserRegisterDto userRegisterDto);
}
