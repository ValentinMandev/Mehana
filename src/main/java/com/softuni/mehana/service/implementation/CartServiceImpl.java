package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.CartItem;
import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.repository.CartRepository;
import com.softuni.mehana.repository.ProductRepository;
import com.softuni.mehana.repository.UserRepository;
import com.softuni.mehana.service.CartService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    ProductRepository productRepository;
    UserRepository userRepository;
    CartRepository cartRepository;

    public CartServiceImpl(ProductRepository productRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public void addToCart(Long productId, int quantity, UserDetails userDetails) {

        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);

        CartEntity cart = user.getCart();

        if (cart == null) {
            cart = new CartEntity();
        }

        ProductEntity product = productRepository.findById(productId).orElse(null);
        CartItem cartItem;

        List<CartItem> existing = cart.getCartItems().stream().filter(i -> i.getProduct().getId().equals(product.getId())).toList();

        if (!existing.isEmpty()) {
            cartItem = existing.get(0);
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        }

        cart.getCartItems().add(cartItem);

        BigDecimal currentPrice;
        if (product.isOnPromotion()) {
            currentPrice = product.getPromoPrice();
        } else {
            currentPrice = product.getPrice();
        }

        BigDecimal itemPrice = currentPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        cartItem.setPrice(currentPrice);
        cartItem.setTotalPrice(itemPrice);

        cart.setPrice(cart.getPrice().add(currentPrice.multiply(BigDecimal.valueOf(quantity))));
        cartRepository.save(cart);

        user.setCart(cart);
        userRepository.save(user);

    }

    @Override
    public CartEntity getCart(UserDetails userDetails) {
        UserEntity user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
        if (user.getCart() == null) {
            CartEntity cart = new CartEntity();
            cart.setPrice(BigDecimal.ZERO);
            return cart;
        }
        return user.getCart();
    }

    @Override
    public Set<CartItem> getCartItems(CartEntity cart) {
        if (cart == null) {
            return new HashSet<>();
        }

        return cart.getCartItems();
    }
}
