package com.softuni.mehana.repository;

import com.softuni.mehana.model.entities.ProductEntity;
import com.softuni.mehana.model.enums.ProductTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByType(ProductTypeEnum productTypeEnum);

    List<ProductEntity> findAllByIsOnPromotion(boolean isOnPromotion);
}
