package com.softuni.mehana.orderAPI.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI();

        openAPI.components(
                new Components()
                        .addSecuritySchemes("bearer-token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ));

        openAPI.setInfo(
                new Info()
                        .description("This is the Order storage micro service.")
                        .title("Mehana order storage")
                        .version("0.0.1")
                        .contact(
                                new Contact()
                                        .name("Valentin Mandev")
                                        .email("valio@gmail.com")
                        )
        );

        return openAPI;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
