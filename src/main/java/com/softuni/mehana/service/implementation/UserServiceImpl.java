package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.dto.UserRegisterDto;
import com.softuni.mehana.model.entities.UserDetailsEntity;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.model.entities.UserInfoEntity;
import com.softuni.mehana.model.enums.UserRoleEnum;
import com.softuni.mehana.repository.UserInfoRepository;
import com.softuni.mehana.repository.UserRepository;
import com.softuni.mehana.repository.UserRoleRepository;
import com.softuni.mehana.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserInfoRepository userInfoRepository;

    public UserServiceImpl(ModelMapper modelMapper, PasswordEncoder passwordEncoder,
                           UserRepository userRepository, UserRoleRepository userRoleRepository,
                           UserInfoRepository userInfoRepository) {
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public void registerUser(UserRegisterDto userRegisterDto) {
        UserInfoEntity userInfo = mapToUserInfoEntity(userRegisterDto);

        UserEntity userEntity = mapToUserEntity(userRegisterDto);
        userEntity.setUserInfo(userInfo);
        userEntity.getRoles().add(userRoleRepository.findByRole(UserRoleEnum.USER).orElse(null));

        userRepository.save(userEntity);
    }

    private UserEntity mapToUserEntity(UserRegisterDto userRegisterDto) {
        UserEntity user = modelMapper.map(userRegisterDto, UserEntity.class);
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        return user;
    }

    private UserInfoEntity mapToUserInfoEntity(UserRegisterDto userRegisterDto) {
        UserInfoEntity userInfoEntity = modelMapper.map(userRegisterDto, UserInfoEntity.class);
        userInfoRepository.save(userInfoEntity);
        return userInfoEntity;
    }

    @Override
    public Optional<UserDetailsEntity> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.getPrincipal() instanceof UserDetailsEntity userDetails) {
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }
}
