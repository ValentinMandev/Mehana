package com.softuni.mehana.service;

import com.softuni.mehana.model.dto.CheckoutDto;
import com.softuni.mehana.model.dto.OrderDetailsDto;
import com.softuni.mehana.model.dto.OrderDto;
import com.softuni.mehana.model.dto.StoreOrderDto;
import com.softuni.mehana.model.entities.UserEntity;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders(Long userId);

    void storeOrder(UserEntity user, CheckoutDto checkoutDto);

    OrderDetailsDto getOrderDetails(Long userId, Long orderId);
}


