package com.softuni.mehana.controller.user;

import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.mock.web.MockHttpSession;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void testLoginAndEditProfile() throws Exception {
        MvcResult result = mockMvc.perform(post("/user/login")
                        .param("username", "valio")
                        .param("password", "1234")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andReturn();

        // Store the session for subsequent requests
        session = (MockHttpSession) result.getRequest().getSession();
    }


    @Test
    public void testGetUserProfile() throws Exception {
        mockMvc.perform(get("/user/edit-profile")
                        .session(session)  // Use the session from the login
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testEditUserProfile() throws Exception {
        mockMvc.perform(post("/user/edit-profile")
                        .session(session)  // Use the session from the login
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("phoneNumber", "0888741852")
                        .param("email", "valio89@gmail.com")
                        .param("firstName", "Валентин")
                        .param("lastName", "Мандев")
                        .param("address", "София, жк. Квартал 1"))
                .andExpect(status().is3xxRedirection());

        Optional<UserEntity> user = userRepository.findByUsername("valio");
        Assertions.assertEquals("0888741852", user.get().getUserInfo().getPhoneNumber());
        Assertions.assertEquals("valio89@gmail.com", user.get().getUserInfo().getEmail());
        Assertions.assertEquals("София, жк. Квартал 1", user.get().getUserInfo().getAddress());
    }

}
