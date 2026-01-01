package com.softuni.mehana.controller;

import com.softuni.mehana.model.dto.AddProductDto;
import com.softuni.mehana.model.dto.UpdateProductDto;
import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/edit-product/{id}")
    public String editProduct(@PathVariable("id") Long id, Model model) {
        UpdateProductDto updateProductDto = productService.updateProductDtoBuilder(id);
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

        ProductEntity product = productService.getProductById(id);
        productService.updateProduct(updateProductDto, product);
        return "redirect:/menu";
    }

    @PostMapping("/disable-product/{id}")
    public String disableProduct(@PathVariable("id") Long id) {
        ProductEntity product = productService.getProductById(id);
        productService.disableProduct(product);
        return "redirect:/menu";
    }

    @GetMapping("/add-product")
    public String addProduct(Model model) {
        model.addAttribute("addProductDto", new AddProductDto());
        return "admin/add-product";
    }

    @PostMapping("/add-product")
    public String addProduct(@Valid AddProductDto addProductDto,
                             BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addProductDto", addProductDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addProductDto", bindingResult);
            return "admin/add-product";
        }

        productService.addProduct(addProductDto);

        return "redirect:/menu";
    }
}
