package com.softuni.mehana.service.implementation;

import com.softuni.mehana.model.dto.*;
import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.CartItemEntity;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.repository.UserRepository;
import com.softuni.mehana.service.CartService;
import com.softuni.mehana.service.OrderService;
import com.softuni.mehana.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


@Service
public class OrderServiceImpl implements OrderService {

    private final RestClient ordersRestClient;
    private final UserRepository userRepository;
    private final UserService userService;
    private final CartService cartService;
    private final ModelMapper modelMapper;


    public OrderServiceImpl(@Qualifier("ordersRestClient") RestClient ordersRestClient, UserRepository userRepository, UserService userService, CartService cartService, ModelMapper modelMapper) {
        this.ordersRestClient = ordersRestClient;
        this.userRepository = userRepository;
        this.userService = userService;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }


    @Override
    public void storeOrder(UserDetails userDetails, CheckoutDto checkoutDto) {
        UserEntity user = userService.getCurrentUser(userDetails);

        StoreOrderDto order = storeOrderDtoBuilder(user, checkoutDto);

        Set<CartItemDto> cartItemDtos = new LinkedHashSet<>();
        CartEntity cart = cartService.getCart(userDetails);

        for (CartItemEntity cartItemEntity : cartService.getCartItems(cart)) {
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


    @Override
    public List<OrderDto> getAllOrders(Long userId) {
        return ordersRestClient
                .get()
                .uri("/orders/all/{user_id}", userId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }


    @Override
    public OrderDetailsDto getOrderDetails(Long userId, Long orderId) {
        return ordersRestClient
                .get()
                .uri("/orders/{user_id}/{orderId}", userId, orderId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>(){});
    }

    private StoreOrderDto storeOrderDtoBuilder(UserEntity user, CheckoutDto checkoutDto) {
        StoreOrderDto order = new StoreOrderDto();

        order.setUserId(user.getId());
        order.setPrice(user.getCart().getPrice());
        order.setTime(LocalDateTime.now());
        order.setAddress(checkoutDto.getAddress());
        order.setFullName(checkoutDto.getFirstName() + " " + checkoutDto.getLastName());

        return order;
    }
}
