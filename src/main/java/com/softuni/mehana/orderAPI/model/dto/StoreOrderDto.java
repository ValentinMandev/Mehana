package com.softuni.mehana.orderAPI.model.dto;

import com.softuni.mehana.orderAPI.model.entities.CartItemEntityAPI;
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

    private Set<CartItemEntityAPI> products;

    private String fullName;

    private String address;

}
