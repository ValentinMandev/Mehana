package com.softuni.mehana.service;

import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.CartItemEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

public interface CartService {
    void addToCart(Long productId, int quantity, UserDetails userDetails);

    CartEntity getCart(UserDetails userDetails);
    Set<CartItemEntity> getCartItems(CartEntity cart);
    void remove(Long id, UserDetails userDetails);

    void clearCart(UserDetails userDetails);
}
