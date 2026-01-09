package com.softuni.mehana.controller;

import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.CartItemEntity;
import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.service.CartService;
import com.softuni.mehana.service.ProductService;
import com.softuni.mehana.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
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
    public ResponseEntity<?> addToCart(@RequestParam("productId") Long productId,
                            @RequestParam("quantity") int quantity,
                            @RequestParam(value = "returnUrl", required = false) String returnUrl,
                            @AuthenticationPrincipal UserDetails userDetails,
                            @RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {

        if (userDetails == null) {
            if ("XMLHttpRequest".equals(requestedWith)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Трябва да влезете в профила си", "redirect", "/login"));
            }
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", "/login")
                    .build();
        }

        UserEntity user = userService.getCurrentUser(userDetails);
        ProductEntity product = productService.getProductById(productId);
        cartService.addToCart(product, quantity, user);

        if ("XMLHttpRequest".equals(requestedWith)) {
		        return ResponseEntity.ok().body(Map.of("success", true, "message", "��������� � �������"));
    }

        if (returnUrl != null && !returnUrl.isEmpty()) {
			        return ResponseEntity.status(HttpStatus.FOUND).header("Location", returnUrl).build();
	    }

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/menu").build();
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
