package com.softuni.mehana.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserDetailsController {

    @GetMapping("/user/profile")
    public String profile() {
        return "profile";
    }
}
