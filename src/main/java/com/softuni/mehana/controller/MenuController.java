package com.softuni.mehana.controller;

import com.softuni.mehana.model.enums.ProductTypeEnum;
import com.softuni.mehana.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MenuController {

    ProductService productService;

    public MenuController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/menu")
    public String menu(Model model) {

        model.addAttribute("starters", productService.getAllByType(ProductTypeEnum.STARTERS));
        model.addAttribute("soups", productService.getAllByType(ProductTypeEnum.SOUPS));
        model.addAttribute("main", productService.getAllByType(ProductTypeEnum.MAIN_COURSES));
        model.addAttribute("desserts", productService.getAllByType(ProductTypeEnum.DESSERTS));
        model.addAttribute("bread", productService.getAllByType(ProductTypeEnum.BREAD));

        return "menu";
    }
}
