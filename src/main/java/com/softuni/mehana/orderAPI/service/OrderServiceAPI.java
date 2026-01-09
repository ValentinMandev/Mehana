package com.softuni.mehana.orderAPI.service;



import com.softuni.mehana.orderAPI.model.dto.OrderDetailsDto;
import com.softuni.mehana.orderAPI.model.dto.OrderDto;
import com.softuni.mehana.orderAPI.model.dto.StoreOrderDto;

import java.util.List;

public interface OrderServiceAPI {
    OrderDetailsDto getOrderDetails(Long userId, Long id);

    List<OrderDto> getAllOrders(Long userId);

    OrderDetailsDto storeOrder(StoreOrderDto storeOrderDto);
}
