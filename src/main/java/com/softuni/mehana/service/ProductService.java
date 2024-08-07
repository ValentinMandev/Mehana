package com.softuni.mehana.service;

import com.softuni.mehana.model.dto.AddProductDto;
import com.softuni.mehana.model.dto.UpdateProductDto;
import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.enums.ProductTypeEnum;

import java.util.List;

public interface ProductService {

    List<ProductEntity> getAllByType(ProductTypeEnum productTypeEnum);

    void updateProduct(UpdateProductDto updateProductDto, ProductEntity product);

    void disableProduct(ProductEntity product);

    void addProduct(AddProductDto addProductDto);

    ProductEntity getProductById(Long id);

    UpdateProductDto updateProductDtoBuilder(Long id);
}
