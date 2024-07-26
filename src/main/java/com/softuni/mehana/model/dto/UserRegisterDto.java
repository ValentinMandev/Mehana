package com.softuni.mehana.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {

    @Size(min = 3, max = 20, message = "{username.length}")
    private String username;

    @Size(min = 3, max = 20, message = "{password.length}")
    private String password;

    @Size(min = 3, max = 20, message = "{password.length}")
    private String confirmPassword;

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

    public static UserRegisterDto empty() {
        return new UserRegisterDto();
    }
}
