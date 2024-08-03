package com.softuni.mehana.utils;

import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.entities.PromoEntity;
import com.softuni.mehana.model.enums.ProductTypeEnum;
import com.softuni.mehana.repository.ProductRepository;
import com.softuni.mehana.repository.PromoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Component
public class RandomizePromotions {

    private final ProductRepository productRepository;
    private final PromoRepository promoRepository;
    private final Random random = new Random();
    private final ResetCarts resetCarts;

    public RandomizePromotions(ProductRepository productRepository, PromoRepository promoRepository, ResetCarts resetCarts) {
        this.productRepository = productRepository;
        this.promoRepository = promoRepository;
        this.resetCarts = resetCarts;
    }

    @Scheduled(cron = "0 50 15 * * *", zone = "GMT+3")
    public void definePromotions() {
        resetCarts.reset();
        clearPromotions();

        setPromotion(ProductTypeEnum.STARTERS);
        setPromotion(ProductTypeEnum.MAIN_COURSES);
        setPromotion(ProductTypeEnum.DESSERTS);
    }

    public void setPromotion(ProductTypeEnum productType) {
        List<ProductEntity> products = productRepository.findAllByType(productType);
        ProductEntity product = products.get(random.nextInt(products.size()));
        product.setOnPromotion(true);
        product.setPromoPrice(product.getPrice().multiply(BigDecimal.valueOf(0.8)));
        productRepository.save(product);

        PromoEntity promoEntity = new PromoEntity();
        promoEntity.setProduct(product);
        promoRepository.save(promoEntity);
    }

    private void clearPromotions() {
        List<ProductEntity> productsOnPromotion = productRepository.findAllByIsOnPromotion(true);

        for (ProductEntity product : productsOnPromotion) {
            product.setOnPromotion(false);
            productRepository.save(product);
        }

        promoRepository.deleteAll();
    }

}
