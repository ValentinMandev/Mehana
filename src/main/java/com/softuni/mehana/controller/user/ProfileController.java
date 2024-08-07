package com.softuni.mehana.controller.user;

import com.softuni.mehana.model.dto.UpdateProfileDto;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/edit-profile")
    public String getProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UpdateProfileDto updateProfileDto = userService.getUpdateProfileDto(userDetails);
        model.addAttribute("updateProfileDto", updateProfileDto);
        return "/profile";
    }

    @PostMapping("/user/edit-profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @Valid UpdateProfileDto updateProfileDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("updateProfileDto", updateProfileDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.updateProfileDto", bindingResult);
            return "profile";
        }

        UserEntity user = userService.getCurrentUser(userDetails);
        userService.updateProfile(updateProfileDto, user);

        return "redirect:/";
    }
}
