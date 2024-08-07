package com.softuni.mehana.init;

import com.softuni.mehana.repository.PromoRepository;
import com.softuni.mehana.utils.RandomizePromotions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(3)
@Component
public class InitializePromotions implements CommandLineRunner {

    private final RandomizePromotions promotions;
    private final PromoRepository promoRepository;

    public InitializePromotions(RandomizePromotions promotions, PromoRepository promoRepository) {
        this.promotions = promotions;
        this.promoRepository = promoRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        if (promoRepository.findAll().isEmpty()) {
            promotions.definePromotions();
        }
    }
}
