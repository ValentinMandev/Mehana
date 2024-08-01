package com.softuni.mehana.utils;

import com.softuni.mehana.repository.CartRepository;
import com.softuni.mehana.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;

@Component
public class ResetCarts {

    UserRepository userRepository;
    CartRepository cartRepository;
    @PersistenceContext
    EntityManager entityManager;

    public ResetCarts(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    @Scheduled(cron = "0 49 15 * * *", zone = "GMT+3")
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

        entityManager.joinTransaction();
        entityManager
                .createQuery("DELETE FROM CartItem")
                .executeUpdate();
    }
}
