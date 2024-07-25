package com.softuni.mehana.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CartItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(nullable = false)
    private int quantity;

}
