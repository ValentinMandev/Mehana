package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.dto.UserRegisterDto;
import com.softuni.mehana.model.entities.UserDetailsEntity;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.model.entities.UserRoleEntity;
import com.softuni.mehana.model.enums.UserRoleEnum;
import com.softuni.mehana.repository.UserDetailsRepository;
import com.softuni.mehana.repository.UserRepository;
import com.softuni.mehana.repository.UserRoleRepository;
import com.softuni.mehana.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserDetailsRepository userDetailsRepository;

    public UserServiceImpl(ModelMapper modelMapper, PasswordEncoder passwordEncoder,
                           UserRepository userRepository, UserRoleRepository userRoleRepository,
                           UserDetailsRepository userDetailsRepository) {
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userDetailsRepository = userDetailsRepository;
    }

    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {
        UserDetailsEntity userDetails = mapToUserDetailsEntity(userRegisterDto);

        UserEntity userEntity = mapToUserEntity(userRegisterDto);
        userEntity.setUserDetails(userDetails);
        userEntity.setRole(userRoleRepository.findByRole(UserRoleEnum.USER).orElse(null));

        userRepository.save(userEntity);
    }

    private UserEntity mapToUserEntity(UserRegisterDto userRegisterDto) {
        UserEntity user = modelMapper.map(userRegisterDto, UserEntity.class);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        return user;
    }

    private UserDetailsEntity mapToUserDetailsEntity(UserRegisterDto userRegisterDto) {
        UserDetailsEntity userDetails = modelMapper.map(userRegisterDto, UserDetailsEntity.class);
        userDetailsRepository.save(userDetails);
        return userDetails;
    }
}
