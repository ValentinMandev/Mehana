package com.softuni.mehana.model.dto;

import com.softuni.mehana.model.enums.ProductTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddProductDto {

    private String name;

    private ProductTypeEnum type;

    private String nameEng;

    private String imageUrl;

    private BigDecimal price;

}
