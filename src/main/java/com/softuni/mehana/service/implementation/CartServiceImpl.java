package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.CartItemEntity;
import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.repository.CartRepository;
import com.softuni.mehana.repository.ProductRepository;
import com.softuni.mehana.repository.UserRepository;
import com.softuni.mehana.service.CartService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        CartItemEntity cartItemEntity;

        List<CartItemEntity> existing = cart.getCartItemEntities().stream().filter(i -> i.getProduct().getId().equals(product.getId())).toList();

        if (!existing.isEmpty()) {
            cartItemEntity = existing.get(0);
            cartItemEntity.setQuantity(cartItemEntity.getQuantity() + quantity);
        } else {
            cartItemEntity = new CartItemEntity();
            cartItemEntity.setProduct(product);
            cartItemEntity.setQuantity(quantity);
        }

        cart.getCartItemEntities().add(cartItemEntity);

        BigDecimal currentPrice;
        if (product.isOnPromotion()) {
            currentPrice = product.getPromoPrice();
        } else {
            currentPrice = product.getPrice();
        }

        BigDecimal itemPrice = currentPrice.multiply(BigDecimal.valueOf(cartItemEntity.getQuantity()));

        cartItemEntity.setPrice(currentPrice);
        cartItemEntity.setTotalPrice(itemPrice);

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
    public Set<CartItemEntity> getCartItems(CartEntity cart) {
        if (cart == null) {
            return new HashSet<>();
        }

        return cart.getCartItemEntities();
    }

    @Override
    public void remove(Long id, UserDetails userDetails) {
        CartEntity cart = getCart(userDetails);
        CartItemEntity cartItemEntity = cart.getCartItemEntities().stream().filter(i -> i.getId().equals(id)).toList().get(0);
        cart.getCartItemEntities().remove(cartItemEntity);
        cart.setPrice(cart.getPrice().subtract(cartItemEntity.getTotalPrice()));
        cartRepository.save(cart);
    }

    @Override
    public void clearCart(UserDetails userDetails) {
        CartEntity cart = getCart(userDetails);
        cart.setCartItemEntities(new HashSet<>());
        cart.setPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }
}
