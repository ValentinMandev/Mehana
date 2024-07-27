package com.softuni.mehana.service;

import com.softuni.mehana.model.dto.UserRegisterDto;
import com.softuni.mehana.model.entities.UserDetailsEntity;

import java.util.Optional;

public interface UserService {
    void registerUser(UserRegisterDto registerDTO);

    Optional<UserDetailsEntity> getCurrentUser();
}
