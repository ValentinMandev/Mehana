package com.softuni.mehana.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class OrderEntity {

    @Column(nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime time;

    @Transient
    private LinkedHashMap<ProductEntity, Integer> products;


}
