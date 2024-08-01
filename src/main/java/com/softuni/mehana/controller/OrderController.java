package com.softuni.mehana.controller;

import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.CartItemEntity;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.service.CartService;
import com.softuni.mehana.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Set;

@Controller
public class OrderController {

    UserService userService;
    CartService cartService;

    public OrderController(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping("/checkout")
    public String getFinalizePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        CartEntity cart = cartService.getCart(userDetails);

        if (cart.getCartItemEntities().isEmpty()) {
            return "redirect:/cart";
        }

        UserEntity user = userService.getCurrentUser(userDetails);
        Set<CartItemEntity> cartItemEntities = cartService.getCartItems(cart);

        model.addAttribute("userInfo", user.getUserInfo());
        model.addAttribute("price", cart.getPrice());
        model.addAttribute("cartItems", cartItemEntities);

        return "checkout";
    }

    @PostMapping("/finalize-order")
    public String checkout(@AuthenticationPrincipal UserDetails userDetails) {

        return "order-successful";
    }
}
