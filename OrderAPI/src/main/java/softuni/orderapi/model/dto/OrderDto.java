package softuni.orderapi.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {

    private Long id;

    private BigDecimal price;

    private LocalDateTime time;

    private String address;

}
