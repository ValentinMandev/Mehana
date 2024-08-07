package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.dto.UpdateProfileDto;
import com.softuni.mehana.model.dto.UserRegisterDto;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.model.entities.UserInfoEntity;
import com.softuni.mehana.model.entities.UserRoleEntity;
import com.softuni.mehana.model.enums.UserRoleEnum;
import com.softuni.mehana.model.userdetails.UserDetailsEntity;
import com.softuni.mehana.repository.UserInfoRepository;
import com.softuni.mehana.repository.UserRepository;
import com.softuni.mehana.repository.UserRoleRepository;
import com.softuni.mehana.service.UserService;
import com.softuni.mehana.service.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    public UserServiceImpl(ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserRepository userRepository,
                           UserRoleRepository userRoleRepository, UserInfoRepository userInfoRepository) {
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

        UserRoleEntity userRole = userRoleRepository.findByRole(UserRoleEnum.USER).orElse(null);

        userEntity.getRoles().add(userRole);

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
    public UserEntity getCurrentUser(UserDetails userDetails) {
        Optional<UserEntity> user = userRepository.findByUsername(userDetails.getUsername());

        if (user.isEmpty()) {
            throw new UserNotFoundException("User " + userDetails.getUsername() + " not found");
        }

        return user.get();
    }

    @Override
    public Optional<UserDetailsEntity> getCurrentUserDetailsEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null &&
                authentication.getPrincipal() instanceof UserDetailsEntity userDetailsEntity) {
            return Optional.of(userDetailsEntity);
        }
        return Optional.empty();
    }

    @Override
    public boolean comparePasswords(UserRegisterDto userRegisterDto) {
        return userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword());
    }

    @Override
    public void updateProfile(UpdateProfileDto updateProfileDto, UserEntity user) {
        UserInfoEntity userInfo = user.getUserInfo();

        if (userInfo == null) {
            userInfo = new UserInfoEntity();
        }

        userInfo.setFirstName(updateProfileDto.getFirstName());
        userInfo.setLastName(updateProfileDto.getLastName());
        userInfo.setEmail(updateProfileDto.getEmail());
        userInfo.setAddress(updateProfileDto.getAddress());
        userInfo.setPhoneNumber(updateProfileDto.getPhoneNumber());

        userInfoRepository.save(userInfo);
    }

    @Override
    public UpdateProfileDto getUpdateProfileDto(UserDetails userDetails) {
        UserEntity user = getCurrentUser(userDetails);
        return modelMapper.map(user.getUserInfo(), UpdateProfileDto.class);
    }

}
