package com.softuni.mehana.controller;

import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.CartItemEntity;
import com.softuni.mehana.service.CartService;
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

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public String getCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        CartEntity cart = cartService.getCart(userDetails);
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
        cartService.addToCart(productId, quantity, userDetails);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @DeleteMapping("/cart/remove/{id}")
    public String removeCartItem(@PathVariable("id") Long id,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        cartService.remove(id, userDetails);
        return "redirect:/cart";
    }

    @DeleteMapping("/cart/remove/all")
    public String clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        cartService.clearCart(userDetails);
        return "redirect:/cart";
    }

}
