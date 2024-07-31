package com.softuni.mehana.init;

import com.softuni.mehana.utils.RandomizePromotions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(3)
@Component
public class InitializePromotions implements CommandLineRunner {

    RandomizePromotions promotions;

    public InitializePromotions(RandomizePromotions promotions) {
        this.promotions = promotions;
    }


    @Override
    public void run(String... args) throws Exception {
        promotions.definePromotions();
    }
}
