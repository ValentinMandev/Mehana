package com.softuni.mehana.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    @ManyToOne
    private UserEntity user;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDateTime time;

    @OneToMany(fetch = FetchType.EAGER)
    private LinkedHashSet<CartItem> products;

}
