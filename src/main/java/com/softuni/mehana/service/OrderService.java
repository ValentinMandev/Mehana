package com.softuni.mehana.service;

import com.softuni.mehana.model.dto.CheckoutDto;
import com.softuni.mehana.model.dto.OrderDto;
import com.softuni.mehana.model.entities.UserEntity;

public interface OrderService {
    void storeOrder(UserEntity user, CheckoutDto checkoutDto);
}


