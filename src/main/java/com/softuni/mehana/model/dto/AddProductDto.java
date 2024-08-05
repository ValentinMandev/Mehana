package com.softuni.mehana.model.dto;

import com.softuni.mehana.model.enums.ProductTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AddProductDto {

    @NotEmpty(message = "{field.empty}")
    private String name;

    private ProductTypeEnum type;

    @NotEmpty(message = "{field.empty}")
    private String nameEng;

    @NotEmpty(message = "{field.empty}")
    private String imageUrl;

    @NotNull(message = "{field.empty}")
    private BigDecimal price;

}
