package com.softuni.mehana.utils;

import com.softuni.mehana.repository.CartRepository;
import com.softuni.mehana.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;

@Component
public class ResetCart {

    UserRepository userRepository;
    CartRepository cartRepository;

    public ResetCart(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Scheduled(cron = "0 59 7 * * *", zone = "UTC")
    public void reset() {
        userRepository.findAll().forEach(u -> {
            u.setCart(null);
            userRepository.save(u);
        });
        cartRepository.findAll().forEach(c -> {
            c.setCartItems(new HashSet<>());
            c.setPrice(BigDecimal.ZERO);
            cartRepository.save(c);
        });
        cartRepository.deleteAll();
    }
}
