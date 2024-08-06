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
import com.softuni.mehana.service.CartService;
import com.softuni.mehana.service.ProductService;
import com.softuni.mehana.service.exception.ProductNotFoundException;
import com.softuni.mehana.service.exception.ProductsOfTypeNotFoundException;
import com.softuni.mehana.service.exception.PromotionsNotFoundException;
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
    private final CartService cartService;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CartRepository cartRepository,
                              UserRepository userRepository, RandomizePromotions randomizePromotions,
                              PromoRepository promoRepository, CartService cartService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.randomizePromotions = randomizePromotions;
        this.promoRepository = promoRepository;
        this.cartService = cartService;
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
    public void updateProduct(UpdateProductDto updateProductDto, Long id) {
        ProductEntity product = getProductById(id);

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
    public void disableProduct(Long id) {
        ProductEntity product = getProductById(id);

        product.setEnabled(false);
        removeFromCarts(product);
        if (product.isOnPromotion()) {
            product.setOnPromotion(false);
            randomizePromotions.setPromotion(product.getType());
            List<PromoEntity> promotions = promoRepository.findAll();

            Optional<PromoEntity> promo = promotions.stream().filter(p -> p.getProduct().getId().equals(product.getId())).findFirst();
            if (promo.isEmpty()) {
                throw new PromotionsNotFoundException("Product with id " + product.getId() + " not found in promotions repository!");
            }

            promoRepository.delete(promo.get());
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
                    for (CartItemEntity cartItemEntity : cartService.getCartItems(cart)) {
                        if (cartItemEntity.getProduct().getId().equals(product.getId())) {
                            cart.getCartItemEntities().remove(cartItemEntity);
                            cartRepository.save(cart);
                            break;
                        }
                    }
                });
    }
}
