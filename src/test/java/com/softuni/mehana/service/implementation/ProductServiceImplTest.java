package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.dto.AddProductDto;
import com.softuni.mehana.model.dto.UpdateProductDto;
import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.enums.ProductTypeEnum;
import com.softuni.mehana.repository.CartRepository;
import com.softuni.mehana.repository.ProductRepository;
import com.softuni.mehana.repository.PromoRepository;
import com.softuni.mehana.repository.UserRepository;
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

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Captor
    private ArgumentCaptor<ProductEntity> productEntityCaptor;
    private ProductServiceImpl toTest;
    @Mock
    private ProductRepository mockProductRepository;
    @Mock
    private CartRepository mockCartRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private RandomizePromotions mockRandomizePromotions;
    @Mock
    private PromoRepository mockPromoRepository;
    private ModelMapper mockModelMapper = new ModelMapper();

    private ProductEntity product;

    @BeforeEach
    void setUp() {
        toTest = new ProductServiceImpl(
                mockProductRepository,
                mockCartRepository,
                mockUserRepository,
                mockRandomizePromotions,
                mockPromoRepository,
                mockModelMapper
        );

        product = new ProductEntity();
        product.setName("Боб чорба");
        product.setNameEng("Beans soup");
        product.setType(ProductTypeEnum.SOUPS);
        product.setPrice(BigDecimal.valueOf(2.50));
        product.setPromoPrice(BigDecimal.valueOf(2.00));
        product.setImageUrl("");
        product.setOnPromotion(true);
        product.setEnabled(true);
    }

    @Test
    void testUpdateProduct() {
        UpdateProductDto updateProductDto = new UpdateProductDto();
        updateProductDto.setId(Long.valueOf(1));
        updateProductDto.setName("Боб чорбааа");
        updateProductDto.setNameEng("Beans soup");
        updateProductDto.setType(ProductTypeEnum.SOUPS);
        updateProductDto.setPrice(BigDecimal.valueOf(5.00));
        updateProductDto.setImageUrl("");

        toTest.updateProduct(updateProductDto, product);

        verify(mockProductRepository).save(productEntityCaptor.capture());

        ProductEntity actualSavedEntity = productEntityCaptor.getValue();

        Assertions.assertNotNull(actualSavedEntity);
        Assertions.assertEquals(BigDecimal.valueOf(5.00), actualSavedEntity.getPrice());
        Assertions.assertEquals("Боб чорбааа", actualSavedEntity.getName());
        Assertions.assertEquals("Beans soup", actualSavedEntity.getNameEng());
    }


    @Test
    void addProduct() {
        AddProductDto addProductDto = new AddProductDto();
        addProductDto.setName("Супа леща");
        addProductDto.setNameEng("Lentils soup");
        addProductDto.setType(ProductTypeEnum.SOUPS);
        addProductDto.setPrice(BigDecimal.valueOf(4.00));
        addProductDto.setImageUrl("LentilsURL");


        toTest.addProduct(addProductDto);

        verify(mockProductRepository).save(productEntityCaptor.capture());

        ProductEntity actualSavedEntity = productEntityCaptor.getValue();

        Assertions.assertNotNull(actualSavedEntity);
        Assertions.assertTrue(actualSavedEntity.isEnabled());
        Assertions.assertEquals(BigDecimal.valueOf(4.00), actualSavedEntity.getPrice());
        Assertions.assertEquals("Супа леща", actualSavedEntity.getName());
        Assertions.assertEquals("Lentils soup", actualSavedEntity.getNameEng());
        Assertions.assertEquals(ProductTypeEnum.SOUPS, actualSavedEntity.getType());
    }

}
