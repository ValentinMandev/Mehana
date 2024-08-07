package com.softuni.mehana.controller.user;

import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class RegisterControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegistration() throws Exception {

        mockMvc.perform(post("/user/register")
                        .param("username", "niki")
                        .param("password", "1234")
                        .param("confirmPassword", "1234")
                        .param("email", "nikolay@gmail.com")
                        .param("firstName", "Nikolay")
                        .param("lastName", "Nikolov")
                        .param("phoneNumber", "0888963963")
                        .param("address", "София, жк Квартал 4")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        Optional<UserEntity> userEntityOpt = userRepository.findByUsername("niki");

        Assertions.assertTrue(userEntityOpt.isPresent());

        UserEntity userEntity = userEntityOpt.get();

        Assertions.assertEquals("Nikolay", userEntity.getUserInfo().getFirstName());
        Assertions.assertEquals("Nikolov", userEntity.getUserInfo().getLastName());
        Assertions.assertEquals("nikolay@gmail.com", userEntity.getUserInfo().getEmail());
        Assertions.assertEquals("0888963963", userEntity.getUserInfo().getPhoneNumber());
        Assertions.assertEquals("София, жк Квартал 4", userEntity.getUserInfo().getAddress());

        Assertions.assertTrue(passwordEncoder.matches("1234", userEntity.getPassword()));
    }
}
