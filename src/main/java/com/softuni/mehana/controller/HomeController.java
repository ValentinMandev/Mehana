package com.softuni.mehana.controller;

import com.softuni.mehana.repository.PromoRepository;
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
    public String home(Model model) {

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
