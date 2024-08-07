package com.softuni.mehana.controller.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.mock.web.MockHttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerIT {

    @Autowired
    private MockMvc mockMvc;

    private MockHttpSession session;

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
    public void testEditUserProfile() throws Exception {
        mockMvc.perform(post("/user/edit-profile")
                        .session(session)  // Use the session from the login
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("phoneNumber", "0882750548")
                        .param("email", "valio89@gmail.com"))
                .andExpect(status().is2xxSuccessful());
    }
}
