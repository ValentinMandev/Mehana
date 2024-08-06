package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.CartItemEntity;
import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.repository.CartRepository;
import com.softuni.mehana.repository.UserRepository;
import com.softuni.mehana.service.CartService;
import com.softuni.mehana.service.ProductService;
import com.softuni.mehana.service.UserService;
import com.softuni.mehana.service.exception.CartItemEntityNotFoundException;
import com.softuni.mehana.service.exception.NullCartItemEntitiesException;
import com.softuni.mehana.service.exception.ProductDisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductService productService;

    public CartServiceImpl(UserRepository userRepository,
                           CartRepository cartRepository, UserService userService, ProductService productService) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    public void addToCart(Long productId, int quantity, UserDetails userDetails) {

        UserEntity user = userService.getCurrentUser(userDetails);

        CartEntity cart = getCart(userDetails);

        ProductEntity product = productService.getProductById(productId);

        if (!product.isEnabled()) {
            throw new ProductDisabledException("Product with id " + productId + " is disabled!");
        }

        CartItemEntity cartItemEntity;

        Optional<CartItemEntity> entity = getCartItems(cart).stream().filter(i -> i.getProduct().getId().equals(product.getId())).findFirst();

        if (entity.isPresent()) {
            cartItemEntity = entity.get();
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
        UserEntity user = userService.getCurrentUser(userDetails);

        if (user.getCart() == null) {
            CartEntity cart = new CartEntity();
            cart.setPrice(BigDecimal.ZERO);
            return cart;
        }

        return user.getCart();
    }

    @Override
    public Set<CartItemEntity> getCartItems(CartEntity cart) {
        if (cart.getCartItemEntities() == null) {
            throw new NullCartItemEntitiesException("Cart id " + cart.getId() + " returns null for cart item entities");
        }

        if (cart.getCartItemEntities().isEmpty()) {
            return new LinkedHashSet<>();
        }

        return cart.getCartItemEntities();
    }

    @Override
    public void remove(Long id, UserDetails userDetails) {
        CartEntity cart = getCart(userDetails);
        Optional<CartItemEntity> entity = getCartItems(cart).stream().filter(i -> i.getId().equals(id)).findFirst();

        if (entity.isPresent()) {
            cart.getCartItemEntities().remove(entity.get());
            cart.setPrice(cart.getPrice().subtract(entity.get().getTotalPrice()));
        } else {
            throw new CartItemEntityNotFoundException("Cart item entity with id " + id + " not found!");
        }

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
