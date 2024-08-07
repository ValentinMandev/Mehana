package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.dto.AddProductDto;
import com.softuni.mehana.model.dto.UserRegisterDto;
import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.model.enums.ProductTypeEnum;
import com.softuni.mehana.repository.*;
import com.softuni.mehana.service.ProductService;
import com.softuni.mehana.service.UserService;
import com.softuni.mehana.utils.RandomizePromotions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    private CartServiceImpl toTest;
    private UserServiceImpl userServiceImpl;
    private ProductServiceImpl productServiceImpl;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private CartRepository mockCartRepository;
    @Mock
    private UserService mockUserService;
    @Mock
    private ProductService mockProductService;
    @Mock
    private ProductRepository mockProductRepository;
    private AddProductDto addProductDto;
    private UserRegisterDto userRegisterDto;
    @Captor
    private ArgumentCaptor<UserEntity> userEntityCaptor;
    @Captor
    private ArgumentCaptor<ProductEntity> productEntityCaptor;
    @Captor
    private ArgumentCaptor<CartEntity> cartEntityCaptor;

    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private UserRoleRepository mockUserRoleRepository;
    @Mock
    private UserInfoRepository mockUserInfoRepository;
    @Mock
    private RandomizePromotions mockRandomizePromotions;
    @Mock
    private PromoRepository mockPromoRepository;
    private ModelMapper mockModelMapper = new ModelMapper();

    @BeforeEach
    void setUp() {
        userServiceImpl = new UserServiceImpl(
                new ModelMapper(),
                mockPasswordEncoder,
                mockUserRepository,
                mockUserRoleRepository,
                mockUserInfoRepository
        );

        productServiceImpl = new ProductServiceImpl(
                mockProductRepository,
                mockCartRepository,
                mockUserRepository,
                mockRandomizePromotions,
                mockPromoRepository,
                mockModelMapper
        );

        toTest = new CartServiceImpl(mockUserRepository,
                mockCartRepository,
                mockUserService,
                mockProductService);

        userRegisterDto = new UserRegisterDto();
        userRegisterDto.setPassword("1234");
        userRegisterDto.setConfirmPassword("1234");
        userRegisterDto.setEmail("nikolay@gmail.com");
        userRegisterDto.setFirstName("Nikolay");
        userRegisterDto.setLastName("Nikolov");
        userRegisterDto.setPhoneNumber("0888963963");
        userRegisterDto.setAddress("гр. София, жк. Квартал 4");

        addProductDto = new AddProductDto();
        addProductDto.setName("Супа леща");
        addProductDto.setNameEng("Lentils soup");
        addProductDto.setType(ProductTypeEnum.SOUPS);
        addProductDto.setPrice(BigDecimal.valueOf(4.00));
        addProductDto.setImageUrl("LentilsURL");
    }


    @Test
    void testAddToCart() {

        userServiceImpl.registerUser(userRegisterDto);
        verify(mockUserRepository).save(userEntityCaptor.capture());
        UserEntity actualSavedUserEntity = userEntityCaptor.getValue();

        productServiceImpl.addProduct(addProductDto);
        verify(mockProductRepository).save(productEntityCaptor.capture());
        ProductEntity actualSavedProductEntity = productEntityCaptor.getValue();

        toTest.addToCart(actualSavedProductEntity, 2, actualSavedUserEntity);

        verify(mockCartRepository).save(cartEntityCaptor.capture());

        CartEntity actualSavedCartEntity = cartEntityCaptor.getValue();

        Assertions.assertNotNull(actualSavedCartEntity);
        Assertions.assertEquals(BigDecimal.valueOf(8.00), actualSavedCartEntity.getPrice());
        Assertions.assertEquals(1, actualSavedCartEntity.getCartItemEntities().size());
    }


    @Test
    void testClearCart() {
        userServiceImpl.registerUser(userRegisterDto);
        verify(mockUserRepository).save(userEntityCaptor.capture());
        UserEntity actualSavedUserEntity = userEntityCaptor.getValue();

        productServiceImpl.addProduct(addProductDto);
        verify(mockProductRepository).save(productEntityCaptor.capture());
        ProductEntity actualSavedProductEntity = productEntityCaptor.getValue();

        toTest.addToCart(actualSavedProductEntity, 2, actualSavedUserEntity);

        verify(mockCartRepository).save(cartEntityCaptor.capture());

        CartEntity actualSavedCartEntity = cartEntityCaptor.getValue();

        toTest.clearCart(actualSavedUserEntity);

        Assertions.assertNotNull(actualSavedCartEntity);
        Assertions.assertEquals(BigDecimal.valueOf(0), actualSavedCartEntity.getPrice());
        Assertions.assertEquals(0, actualSavedCartEntity.getCartItemEntities().size());
    }

}
