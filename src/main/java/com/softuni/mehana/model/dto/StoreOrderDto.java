package com.softuni.mehana.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class StoreOrderDto {

    private Long userId;

    private BigDecimal price;

    private LocalDateTime time;

    private Set<CartItemDto> products;

    private String fullName;

    private String address;
}
