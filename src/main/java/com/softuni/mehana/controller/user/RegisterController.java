package com.softuni.mehana.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {

    @GetMapping("/user/register")
    public String register() {
        return "register";
    }
}
