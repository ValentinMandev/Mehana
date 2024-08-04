package com.softuni.mehana.service;

import com.softuni.mehana.model.dto.UpdateProductDto;
import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.enums.ProductTypeEnum;

import java.util.List;

public interface ProductService {

    List<ProductEntity> getAll(ProductTypeEnum productTypeEnum);

    void updateProduct(UpdateProductDto updateProductDto, ProductEntity product);

    void disableProduct(ProductEntity product);
}
