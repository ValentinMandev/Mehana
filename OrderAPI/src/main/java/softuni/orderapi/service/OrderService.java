package softuni.orderapi.service;

import softuni.orderapi.model.dto.OrderDetailsDto;
import softuni.orderapi.model.dto.OrderDto;
import softuni.orderapi.model.dto.StoreOrderDto;

import java.util.List;

public interface OrderService {
    OrderDetailsDto getOrderDetails(Long userId, Long id);

    List<OrderDto> getAllOrders(Long userId);

    OrderDetailsDto storeOrder(StoreOrderDto storeOrderDto);
}
