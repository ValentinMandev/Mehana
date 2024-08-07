package com.softuni.mehana.controller;

import com.softuni.mehana.model.entities.CartItemEntity;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
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
    public void testAddToCart() throws Exception {
        mockMvc.perform(post("/cart/add")
                        .param("productId", String.valueOf(Long.valueOf(3)))
                        .param("quantity", String.valueOf(2))
                        .session(session)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                        .andExpect(status().is3xxRedirection());

        Optional<UserEntity> user = userRepository.findByUsername("valio");
        CartItemEntity cartItem = user.get().getCart().getCartItemEntities()
                .stream()
                .filter(i -> i.getProduct().getId().equals(Long.valueOf(3)))
                .findFirst().orElse(null);

        Assertions.assertTrue(user.get().getCart().getCartItemEntities().contains(cartItem));

        mockMvc.perform(delete("/cart/remove/{id}", cartItem.getId())
                        .session(session)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection());
    }


    @Test
    public void testClearCart() throws Exception {
        mockMvc.perform(delete("/cart/remove/all")
                        .session(session)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection());

        Optional<UserEntity> user = userRepository.findByUsername("valio");
        Assertions.assertTrue(user.get().getCart().getCartItemEntities().isEmpty());
    }


    @Test
    public void testGetCart() throws Exception {
        mockMvc.perform(get("/cart")
                        .session(session)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is2xxSuccessful());
    }
}
