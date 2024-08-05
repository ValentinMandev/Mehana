package com.softuni.mehana.model.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckoutDto {

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address;
}
