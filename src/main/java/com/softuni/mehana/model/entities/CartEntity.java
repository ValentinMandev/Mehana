package com.softuni.mehana.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class CartEntity extends BaseEntity {

    @Transient
    private LinkedHashMap<ProductEntity, Integer> products;

    @Column(nullable = false)
    private BigDecimal price;

}
