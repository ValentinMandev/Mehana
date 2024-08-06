package com.softuni.mehana.service;

import com.softuni.mehana.model.dto.UpdateProfileDto;
import com.softuni.mehana.model.dto.UserRegisterDto;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.model.userdetails.UserDetailsEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserService {
    void registerUser(UserRegisterDto registerDTO);

    UserEntity getCurrentUser(UserDetails userDetails);

    Optional<UserDetailsEntity> getCurrentUserDetailsEntity();

    boolean comparePasswords(UserRegisterDto userRegisterDto);

    void updateProfile(UpdateProfileDto updateProfileDto, UserDetails userDetails);

    UpdateProfileDto getUpdateProfileDto(UserDetails userDetails);
}
