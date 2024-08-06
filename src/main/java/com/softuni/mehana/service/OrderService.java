package com.softuni.mehana.service;

import com.softuni.mehana.model.dto.CheckoutDto;
import com.softuni.mehana.model.dto.OrderDetailsDto;
import com.softuni.mehana.model.dto.OrderDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface OrderService {
    List<OrderDto> getAllOrders(UserDetails userDetails);

    void storeOrder(UserDetails userDetails, CheckoutDto checkoutDto);

    OrderDetailsDto getOrderDetails(UserDetails userDetails, Long orderId);
}


