package com.softuni.mehana.controller;

import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.enums.ProductTypeEnum;
import com.softuni.mehana.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;
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
    public void testGetAddProductPage() throws Exception {
        mockMvc.perform(get("/add-product")
                        .session(session)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void testGetEditProductPage() throws Exception {
        mockMvc.perform(get("/edit-product/{id}", Long.valueOf(20))
                        .session(session)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is2xxSuccessful());

        Assertions.assertTrue(productRepository.findById(Long.valueOf(20)).get()
                .getName().equals("Пилешка пържола бут"));
    }

    @Test
    public void testAddEditAndRemoveProduct() throws Exception {
        mockMvc.perform(post("/add-product")
                        .param("name", "Спаначена супа")
                        .param("nameEng", "Spinach soup")
                        .param("type", ProductTypeEnum.SOUPS.name())
                        .param("imageUrl", "spsoupURL")
                        .param("price", String.valueOf(BigDecimal.valueOf(3.50)))
                        .session(session)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection());

        Optional<ProductEntity> product = productRepository.findAllByType(ProductTypeEnum.SOUPS)
                .stream().filter(p -> p.getName().equals("Спаначена супа")).findFirst();
        Assertions.assertTrue(product.isPresent());
        Assertions.assertTrue(product.get().getNameEng().equals("Spinach soup"));


        mockMvc.perform(post("/edit-product/{id}", product.get().getId())
                        .param("name", "Спаначенааа супа")
                        .param("nameEng", "Spinach soup")
                        .param("type", ProductTypeEnum.SOUPS.name())
                        .param("imageUrl", "spsoupURL")
                        .param("price", String.valueOf(BigDecimal.valueOf(4.5)))
                        .session(session)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection());

        product = productRepository.findAllByType(ProductTypeEnum.SOUPS)
                .stream().filter(p -> p.getName().equals("Спаначенааа супа")).findFirst();
        Assertions.assertTrue(product.isPresent());


        mockMvc.perform(post("/disable-product/{id}", product.get().getId())
                        .session(session)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection());

        product = productRepository.findAllByType(ProductTypeEnum.SOUPS)
                .stream().filter(p -> p.getName().equals("Спаначенааа супа")).findFirst();
        Assertions.assertTrue(product.isPresent());
        Assertions.assertFalse(product.get().isEnabled());


        productRepository.delete(product.get());
    }

}
