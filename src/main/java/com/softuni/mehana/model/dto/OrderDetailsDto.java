package com.softuni.mehana.model.dto;

import com.softuni.mehana.model.entities.CartItemEntity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class OrderDetailsDto {

    private Long id;

    private BigDecimal price;

    private LocalDateTime time;

    private Set<CartItemDto> products;

    private String fullName;

    private String address;
}
