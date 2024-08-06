package com.softuni.mehana.controller;

import com.softuni.mehana.service.PromoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PromoService promoService;

    public HomeController(PromoService promoService) {
        this.promoService = promoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("promotions", promoService.getAllPromotions());
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
