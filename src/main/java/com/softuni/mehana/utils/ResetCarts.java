package com.softuni.mehana.utils;

import com.softuni.mehana.repository.CartRepository;
import com.softuni.mehana.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;

@Component
public class ResetCarts {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public ResetCarts(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    public void reset() {
        userRepository.findAll().forEach(u -> {
            u.setCart(null);
            userRepository.save(u);
        });
        cartRepository.findAll().forEach(c -> {
            c.setCartItemEntities(new HashSet<>());
            c.setPrice(BigDecimal.ZERO);
            cartRepository.save(c);
        });
        cartRepository.deleteAll();

        entityManager.joinTransaction();
        entityManager
                .createQuery("DELETE FROM CartItemEntityAPI")
                .executeUpdate();
    }
}
