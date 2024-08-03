package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.dto.CartItemDto;
import com.softuni.mehana.model.dto.CheckoutDto;
import com.softuni.mehana.model.dto.OrderDto;
import com.softuni.mehana.model.entities.CartItemEntity;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.repository.UserRepository;
import com.softuni.mehana.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;


@Service
public class OrderServiceImpl implements OrderService {

    private final RestClient ordersRestClient;
    private final UserRepository userRepository;
    private ModelMapper modelMapper;


    public OrderServiceImpl(@Qualifier("ordersRestClient") RestClient ordersRestClient, UserRepository userRepository, ModelMapper modelMapper) {
        this.ordersRestClient = ordersRestClient;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public void storeOrder(UserEntity user, CheckoutDto checkoutDto) {
        OrderDto order = new OrderDto();
        order.setUserId(user.getId());
        order.setPrice(user.getCart().getPrice());
        order.setTime(LocalDateTime.now());
        order.setAddress(checkoutDto.getAddress());
        order.setFullName(checkoutDto.getFirstName() + " " + checkoutDto.getLastName());

        Set<CartItemDto> cartItemDtos = new LinkedHashSet<>();

        for (CartItemEntity cartItemEntity : user.getCart().getCartItemEntities()) {
            CartItemDto cartItemDto = modelMapper.map(cartItemEntity, CartItemDto.class);
            cartItemDto.setProductName(cartItemEntity.getProduct().getName());
            cartItemDtos.add(cartItemDto);
        }

        order.setProducts(cartItemDtos);

        ordersRestClient
            .post()
            .uri("/orders")
            .body(order)
            .retrieve();

        user.setCart(null);
        userRepository.save(user);
    }
}
