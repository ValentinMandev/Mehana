package com.softuni.mehana.utils;

import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.enums.ProductTypeEnum;
import com.softuni.mehana.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Component
public class RandomizePromotions {

    private final ProductRepository productRepository;
    private final Random random = new Random();

    public RandomizePromotions(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity getRandomProduct(ProductTypeEnum productType) {
        List<ProductEntity> products = productRepository.findAllByType(productType);
        ProductEntity product = products.get(random.nextInt(products.size()));
        product.setOnPromotion(true);
        product.setPromoPrice(product.getPrice().multiply(BigDecimal.valueOf(0.8)));
        productRepository.save(product);
        return product;
    }

    public void clearPromotions() {
        List<ProductEntity> productsOnPromotion = productRepository.findAllByIsOnPromotion(true);

        for (ProductEntity product : productsOnPromotion) {
            product.setOnPromotion(false);
            productRepository.save(product);
        }
    }

}
