package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.dto.AddProductDto;
import com.softuni.mehana.model.dto.UpdateProductDto;
import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.CartItemEntity;
import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.entities.PromoEntity;
import com.softuni.mehana.model.enums.ProductTypeEnum;
import com.softuni.mehana.repository.CartRepository;
import com.softuni.mehana.repository.ProductRepository;
import com.softuni.mehana.repository.PromoRepository;
import com.softuni.mehana.repository.UserRepository;
import com.softuni.mehana.service.ProductService;
import com.softuni.mehana.utils.RandomizePromotions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final RandomizePromotions randomizePromotions;
    private final PromoRepository promoRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CartRepository cartRepository, UserRepository userRepository, RandomizePromotions randomizePromotions, PromoRepository promoRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.randomizePromotions = randomizePromotions;
        this.promoRepository = promoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductEntity> getAll(ProductTypeEnum productTypeEnum) {
        return productRepository.findAllByType(productTypeEnum);
    }

    @Override
    public void updateProduct(UpdateProductDto updateProductDto, ProductEntity product) {
        product.setName(updateProductDto.getName());
        product.setNameEng(updateProductDto.getNameEng());
        product.setType(updateProductDto.getType());
        product.setPrice(updateProductDto.getPrice());
        if (product.isOnPromotion()) {
            product.setPromoPrice(updateProductDto.getPrice().multiply(BigDecimal.valueOf(0.8)));
        }
        product.setImageUrl(updateProductDto.getImageUrl());

        productRepository.save(product);
        removeFromCarts(product);
    }

    @Override
    public void disableProduct(ProductEntity product) {
        product.setEnabled(false);
        removeFromCarts(product);
        if (product.isOnPromotion()) {
            product.setOnPromotion(false);
            randomizePromotions.setPromotion(product.getType());
            List<PromoEntity> promotions = promoRepository.findAll();
            PromoEntity promo = promotions.stream().filter(p -> p.getProduct().getId().equals(product.getId())).findFirst().orElse(null);
            promoRepository.delete(promo);
        }
        productRepository.save(product);
    }

    @Override
    public void addProduct(AddProductDto addProductDto) {
        ProductEntity product = modelMapper.map(addProductDto, ProductEntity.class);
        product.setOnPromotion(false);
        product.setEnabled(true);
        product.setPromoPrice(addProductDto.getPrice().multiply(BigDecimal.valueOf(0.8)));
        productRepository.save(product);
    }

    private void removeFromCarts(ProductEntity product) {
        userRepository.findAll()
                .stream()
                .filter(u -> u.getCart() != null)
                .forEach(u -> {
                    CartEntity cart = u.getCart();
                    for (CartItemEntity cartItemEntity : cart.getCartItemEntities()) {
                        if (cartItemEntity.getProduct().getId().equals(product.getId())) {
                            cart.getCartItemEntities().remove(cartItemEntity);
                            cartRepository.save(cart);
                            break;
                        }
                    }
                });
    }
}
