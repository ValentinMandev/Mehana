package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.dto.UpdateProfileDto;
import com.softuni.mehana.model.dto.UserRegisterDto;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.repository.UserInfoRepository;
import com.softuni.mehana.repository.UserRepository;
import com.softuni.mehana.repository.UserRoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private UserServiceImpl toTest;

    @Captor
    private ArgumentCaptor<UserEntity> userEntityCaptor;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @Mock
    private UserRoleRepository mockUserRoleRepository;

    @Mock
    private UserInfoRepository mockUserInfoRepository;

    private UserRegisterDto userRegisterDto;


    @BeforeEach
    void setUp() {

        toTest = new UserServiceImpl(
                new ModelMapper(),
                mockPasswordEncoder,
                mockUserRepository,
                mockUserRoleRepository,
                mockUserInfoRepository
        );

        userRegisterDto = new UserRegisterDto();
        userRegisterDto.setPassword("1234");
        userRegisterDto.setConfirmPassword("1234");
        userRegisterDto.setEmail("nikolay@gmail.com");
        userRegisterDto.setFirstName("Nikolay");
        userRegisterDto.setLastName("Nikolov");
        userRegisterDto.setPhoneNumber("0888963963");
        userRegisterDto.setAddress("гр. София, жк. Квартал 4");

    }

    @Test
    void testUserRegistration() {
        when(mockPasswordEncoder.encode(userRegisterDto.getPassword()))
                .thenReturn(userRegisterDto.getPassword() + userRegisterDto.getPassword());


        toTest.registerUser(userRegisterDto);

        verify(mockUserRepository).save(userEntityCaptor.capture());

        UserEntity actualSavedEntity = userEntityCaptor.getValue();

        Assertions.assertNotNull(actualSavedEntity);
        Assertions.assertEquals(userRegisterDto.getUsername(), actualSavedEntity.getUsername());
        Assertions.assertEquals(userRegisterDto.getFirstName(), actualSavedEntity.getUserInfo().getFirstName());
        Assertions.assertEquals(userRegisterDto.getLastName(), actualSavedEntity.getUserInfo().getLastName());
        Assertions.assertEquals(userRegisterDto.getEmail(), actualSavedEntity.getUserInfo().getEmail());
        Assertions.assertEquals(userRegisterDto.getAddress(), actualSavedEntity.getUserInfo().getAddress());
        Assertions.assertEquals(userRegisterDto.getPhoneNumber(), actualSavedEntity.getUserInfo().getPhoneNumber());
        Assertions.assertEquals(userRegisterDto.getPassword() + userRegisterDto.getPassword(),
                actualSavedEntity.getPassword());
    }


    @Test
    void testComparePasswords() {
        userRegisterDto.setPassword("1234");
        userRegisterDto.setConfirmPassword("1234");
        Assertions.assertTrue(toTest.comparePasswords(userRegisterDto));

        userRegisterDto.setPassword("1234");
        userRegisterDto.setConfirmPassword("5678");
        Assertions.assertFalse(toTest.comparePasswords(userRegisterDto));

    }

    @Test
    void testUpdateProfile() {
        when(mockPasswordEncoder.encode(userRegisterDto.getPassword()))
                .thenReturn(userRegisterDto.getPassword() + userRegisterDto.getPassword());
        toTest.registerUser(userRegisterDto);
        verify(mockUserRepository).save(userEntityCaptor.capture());
        UserEntity actualSavedEntity = userEntityCaptor.getValue();

        UpdateProfileDto updateProfileDto = new UpdateProfileDto();
        updateProfileDto.setFirstName("Stefan");
        updateProfileDto.setLastName("Stefanov");
        updateProfileDto.setEmail("stefan@gmail.com");
        updateProfileDto.setPhoneNumber(actualSavedEntity.getUserInfo().getPhoneNumber());
        updateProfileDto.setAddress(actualSavedEntity.getUserInfo().getAddress());

        toTest.updateProfile(updateProfileDto, actualSavedEntity);

        verify(mockUserRepository).save(userEntityCaptor.capture());

        actualSavedEntity = userEntityCaptor.getValue();

        Assertions.assertNotNull(actualSavedEntity);
        Assertions.assertEquals(userRegisterDto.getUsername(), actualSavedEntity.getUsername());
        Assertions.assertEquals(updateProfileDto.getFirstName(), actualSavedEntity.getUserInfo().getFirstName());
        Assertions.assertEquals(updateProfileDto.getLastName(), actualSavedEntity.getUserInfo().getLastName());
        Assertions.assertEquals(updateProfileDto.getEmail(), actualSavedEntity.getUserInfo().getEmail());
        Assertions.assertEquals(updateProfileDto.getAddress(), actualSavedEntity.getUserInfo().getAddress());
        Assertions.assertEquals(updateProfileDto.getPhoneNumber(), actualSavedEntity.getUserInfo().getPhoneNumber());
    }
}
