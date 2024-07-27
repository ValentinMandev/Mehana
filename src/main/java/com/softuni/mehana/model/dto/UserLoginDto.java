package com.softuni.mehana.model.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {

    @Size(min = 3, max = 20, message = "{username.length}")
    private String username;

    @Size(min = 3, max = 20, message = "{password.length}")
    private String password;

    public static UserLoginDto empty() {
        return new UserLoginDto();
    }
}
