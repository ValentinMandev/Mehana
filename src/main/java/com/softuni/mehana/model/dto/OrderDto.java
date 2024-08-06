package com.softuni.mehana.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderDto {

    private Long id;

    private BigDecimal price;

    private LocalDateTime time;

    private String address;
}
