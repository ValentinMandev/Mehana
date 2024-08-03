package com.softuni.mehana.model.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartItemDto {

    private String productName;

    private int quantity;

    private BigDecimal price;

    private BigDecimal totalPrice;
}
