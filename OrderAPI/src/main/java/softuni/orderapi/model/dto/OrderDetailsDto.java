package softuni.orderapi.model.dto;

import lombok.Getter;
import lombok.Setter;
import softuni.orderapi.model.entities.CartItemEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class OrderDetailsDto {

    private Long id;

    private BigDecimal price;

    private LocalDateTime time;

    private Set<CartItemEntity> products;

    private String fullName;

    private String address;
}
