package softuni.orderapi.service;

import softuni.orderapi.model.dto.OrderDto;
import softuni.orderapi.model.dto.StoreOrderDto;

import java.util.List;

public interface OrderService {
    OrderDto getOrderById(Long userId, Long id);

    List<OrderDto> getAllOrders(Long userId);

    OrderDto storeOrder(StoreOrderDto storeOrderDto);
}
