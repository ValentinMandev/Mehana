package com.softuni.mehana.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class CartEntity extends BaseEntity {

    @OneToMany(fetch = FetchType.EAGER)
    private LinkedHashSet<CartItem> products;

    @Column(nullable = false)
    private BigDecimal price;

}
