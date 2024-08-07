package com.softuni.mehana.controller;

import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.CartItemEntity;
import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.service.CartService;
import com.softuni.mehana.service.ProductService;
import com.softuni.mehana.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;

    public CartController(CartService cartService, UserService userService, ProductService productService) {
        this.cartService = cartService;
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/cart")
    public String getCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getCurrentUser(userDetails);
        CartEntity cart = cartService.getCart(user);
        Set<CartItemEntity> cartItemEntities = cartService.getCartItems(cart);

        model.addAttribute("price", cart.getPrice());
        model.addAttribute("cartItems", cartItemEntities);

        return "cart";
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            @AuthenticationPrincipal UserDetails userDetails,
                            HttpServletRequest request) {
        UserEntity user = userService.getCurrentUser(userDetails);
        ProductEntity product = productService.getProductById(productId);
        cartService.addToCart(product, quantity, user);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @DeleteMapping("/cart/remove/{id}")
    public String removeCartItem(@PathVariable("id") Long id,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getCurrentUser(userDetails);
        cartService.remove(id, user);
        return "redirect:/cart";
    }

    @DeleteMapping("/cart/remove/all")
    public String clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getCurrentUser(userDetails);
        cartService.clearCart(user);
        return "redirect:/cart";
    }

}
