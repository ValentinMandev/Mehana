package com.softuni.mehana.controller;

import com.softuni.mehana.model.enums.ProductTypeEnum;
import com.softuni.mehana.model.userdetails.UserDetailsEntity;
import com.softuni.mehana.utils.RandomizePromotions;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final RandomizePromotions promotions;

    public HomeController(RandomizePromotions promotions) {
        this.promotions = promotions;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        if (userDetails instanceof UserDetailsEntity userDetailsEntity) {
            model.addAttribute("welcomeMessage", userDetailsEntity.getFirstName());
        }

        promotions.clearPromotions();

        model.addAttribute("promotionStarter", promotions.getRandomProduct(ProductTypeEnum.STARTERS));
        model.addAttribute("promotionMain", promotions.getRandomProduct(ProductTypeEnum.MAIN_COURSES));
        model.addAttribute("promotionDessert", promotions.getRandomProduct(ProductTypeEnum.DESSERTS));

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
