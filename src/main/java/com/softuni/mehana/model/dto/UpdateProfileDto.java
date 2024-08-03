package com.softuni.mehana.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileDto {

    @NotEmpty(message = "{field.empty}")
    private String email;

    @NotEmpty(message = "{field.empty}")
    private String firstName;

    @NotEmpty(message = "{field.empty}")
    private String lastName;

    @NotEmpty(message = "{field.empty}")
    private String phoneNumber;

    @NotEmpty(message = "{field.empty}")
    private String address;
}
