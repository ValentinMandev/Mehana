package com.softuni.mehana.controller.user;

import com.softuni.mehana.model.dto.UserRegisterDto;
import com.softuni.mehana.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user/register")
    public String register(Model model) {

        if (!model.containsAttribute("userRegisterDto")) {
            model.addAttribute("userRegisterDto", UserRegisterDto.empty());
        }

        return "register";
    }

    @PostMapping("/user/register")
    public String register(@Valid UserRegisterDto userRegisterDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (!userService.comparePasswords(userRegisterDto)) {
            bindingResult.rejectValue("passwordMatching", "userRegisterDto", "Паролите не съвпадат");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegisterDto", userRegisterDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterDto", bindingResult);
            return "register";
        }

        userService.registerUser(userRegisterDto);
        return "redirect:/";
    }
}
