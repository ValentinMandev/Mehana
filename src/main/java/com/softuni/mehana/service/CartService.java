package com.softuni.mehana.service;

import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.CartItem;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

public interface CartService {
    void addToCart(Long productId, int quantity, UserDetails userDetails);

    CartEntity getCart(UserDetails userDetails);
    Set<CartItem> getCartItems(CartEntity cart);
}
