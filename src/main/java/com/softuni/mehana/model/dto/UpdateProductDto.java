package com.softuni.mehana.model.dto;

import com.softuni.mehana.model.enums.ProductTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateProductDto {

    private Long id;

    @NotEmpty(message = "{field.empty}")
    private String name;

    @NotEmpty(message = "{field.empty}")
    private String nameEng;

    private ProductTypeEnum type;

    @NotNull(message = "{field.empty}")
    private BigDecimal price;

    @NotEmpty(message = "{field.empty}")
    private String imageUrl;

}
