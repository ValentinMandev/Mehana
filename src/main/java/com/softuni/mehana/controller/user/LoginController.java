package com.softuni.mehana.controller.user;

import com.softuni.mehana.model.dto.UserLoginDto;
import com.softuni.mehana.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/login")
    public String login(Model model) {

        if (!model.containsAttribute("userLoginDto")) {
            model.addAttribute("userLoginDto", UserLoginDto.empty());
        }

        return "login";
    }

}
