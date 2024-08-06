package com.softuni.mehana.service;

import com.softuni.mehana.model.dto.CheckoutDto;
import com.softuni.mehana.model.dto.OrderDetailsDto;
import com.softuni.mehana.model.dto.OrderDto;
import com.softuni.mehana.model.dto.StoreOrderDto;
import com.softuni.mehana.model.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders(Long userId);

    void storeOrder(UserDetails userDetails, CheckoutDto checkoutDto);

    OrderDetailsDto getOrderDetails(Long userId, Long orderId);
}


