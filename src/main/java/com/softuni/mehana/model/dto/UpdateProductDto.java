package com.softuni.mehana.model.dto;

import com.softuni.mehana.model.enums.ProductTypeEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateProductDto {

    private Long id;

    private String name;

    private String nameEng;

    private BigDecimal price;

    private String imageUrl;

}
