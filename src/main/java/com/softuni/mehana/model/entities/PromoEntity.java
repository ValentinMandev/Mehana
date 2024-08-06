package com.softuni.mehana.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "promotions")
public class PromoEntity extends BaseEntity {

    @OneToOne
    private ProductEntity product;
}
