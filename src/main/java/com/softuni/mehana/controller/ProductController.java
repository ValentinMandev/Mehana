package com.softuni.mehana.controller;

import com.softuni.mehana.model.dto.UpdateProductDto;
import com.softuni.mehana.model.dto.UpdateProfileDto;
import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.enums.ProductTypeEnum;
import com.softuni.mehana.repository.ProductRepository;
import com.softuni.mehana.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {

    ProductRepository productRepository;
    ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping("/edit-product/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        UpdateProductDto updateProductDto = new UpdateProductDto();
        ProductEntity product = productRepository.findById(id).orElse(null);

        updateProductDto.setId(product.getId());
        updateProductDto.setName(product.getName());
        updateProductDto.setNameEng(product.getNameEng());
        updateProductDto.setPrice(product.getPrice());
        updateProductDto.setImageUrl(product.getImageUrl());

        model.addAttribute("updateProductDto", updateProductDto);
        return "admin/edit-product";
    }

    @PostMapping("/edit-product/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @Valid UpdateProductDto updateProductDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("updateProductDto", updateProductDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.updateProductDto", bindingResult);
            return "admin/edit-product";
        }

        ProductEntity product = productRepository.findById(id).orElse(null);

        productService.updateProduct(updateProductDto, product);

        return "redirect:/menu";
    }

    @PostMapping("/disable-product/{id}")
    public String disableProduct(@PathVariable("id") Long id) {
        ProductEntity product = productRepository.findById(id).orElse(null);
        productService.disableProduct(product);
        return "redirect:/menu";
    }
}
