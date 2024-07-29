package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.enums.ProductTypeEnum;
import com.softuni.mehana.repository.ProductRepository;
import com.softuni.mehana.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductEntity> getAll(ProductTypeEnum productTypeEnum) {
        return productRepository.findAllByType(productTypeEnum);
    }
}
