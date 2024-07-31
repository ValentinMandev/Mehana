package com.softuni.mehana.controller;

import com.softuni.mehana.model.userdetails.UserDetailsEntity;
import com.softuni.mehana.repository.PromoRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PromoRepository promoRepository;

    public HomeController(PromoRepository promoRepository) {
        this.promoRepository = promoRepository;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        if (userDetails instanceof UserDetailsEntity userDetailsEntity) {
            model.addAttribute("welcomeMessage", userDetailsEntity.getFirstName());
        }

        model.addAttribute("promotions", promoRepository.findAll());

        return "home";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/gallery")
    public String gallery() {
        return "gallery";
    }

}
