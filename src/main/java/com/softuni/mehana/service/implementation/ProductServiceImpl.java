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
import com.softuni.mehana.service.exception.ProductNotFoundException;
import com.softuni.mehana.service.exception.ProductsOfTypeNotFoundException;
import com.softuni.mehana.utils.RandomizePromotions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final RandomizePromotions randomizePromotions;
    private final PromoRepository promoRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CartRepository cartRepository,
                              UserRepository userRepository, RandomizePromotions randomizePromotions,
                              PromoRepository promoRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.randomizePromotions = randomizePromotions;
        this.promoRepository = promoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ProductEntity> getAllByType(ProductTypeEnum productTypeEnum) {
        List<ProductEntity> products = productRepository.findAllByType(productTypeEnum);

        if (products.isEmpty()) {
            throw new ProductsOfTypeNotFoundException("No products of type " + productTypeEnum.name() + " were found!");
        }

        return products;
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
        productRepository.save(product);
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

    @Override
    public ProductEntity getProductById(Long id) {
        Optional<ProductEntity> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new ProductNotFoundException("Product with id " + id + " not found!");
        }

        return product.get();
    }

    @Override
    public UpdateProductDto updateProductDtoBuilder(Long id) {
        UpdateProductDto updateProductDto = new UpdateProductDto();
        ProductEntity product = getProductById(id);

        updateProductDto.setId(product.getId());
        updateProductDto.setName(product.getName());
        updateProductDto.setNameEng(product.getNameEng());
        updateProductDto.setType(product.getType());
        updateProductDto.setPrice(product.getPrice());
        updateProductDto.setImageUrl(product.getImageUrl());

        return updateProductDto;
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
                            cart.setPrice(cart.getPrice().subtract(cartItemEntity.getTotalPrice()));
                            cartRepository.save(cart);
                            break;
                        }
                    }
                });
    }
}
