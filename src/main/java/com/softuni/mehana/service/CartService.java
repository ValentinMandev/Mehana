package com.softuni.mehana.service;

import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.CartItemEntity;
import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.entities.UserEntity;

import java.util.Set;

public interface CartService {
    void addToCart(ProductEntity product, int quantity, UserEntity user);
    CartEntity getCart(UserEntity user);
    Set<CartItemEntity> getCartItems(CartEntity cart);
    void remove(Long id, UserEntity user);
    void clearCart(UserEntity user);
}
