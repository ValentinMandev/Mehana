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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void testGetRegisterPage() throws Exception {
        mockMvc.perform(get("/user/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void testRegistration() throws Exception {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String time = now.format(formatter);

        mockMvc.perform(post("/user/register")
                        .param("username", "niki" + time)
                        .param("password", "1234")
                        .param("confirmPassword", "1234")
                        .param("email", "nikolay@gmail.com" + time)
                        .param("firstName", "Nikolay")
                        .param("lastName", "Nikolov")
                        .param("phoneNumber", "0888963963" + time)
                        .param("address", "София, жк Квартал 4")
                        .with(csrf())
                ).andExpect(status().is3xxRedirection());

        Optional<UserEntity> userEntityOpt = userRepository.findByUsername("niki"  + time);

        Assertions.assertTrue(userEntityOpt.isPresent());

        UserEntity userEntity = userEntityOpt.get();

        Assertions.assertEquals("Nikolay", userEntity.getUserInfo().getFirstName());
        Assertions.assertEquals("Nikolov", userEntity.getUserInfo().getLastName());
        Assertions.assertEquals("София, жк Квартал 4", userEntity.getUserInfo().getAddress());
        Assertions.assertTrue(passwordEncoder.matches("1234", userEntity.getPassword()));
    }

}
