package com.softuni.mehana.orderAPI.service.implementation;

import com.softuni.mehana.orderAPI.model.dto.OrderDetailsDto;
import com.softuni.mehana.orderAPI.model.dto.OrderDto;
import com.softuni.mehana.orderAPI.model.dto.StoreOrderDto;
import com.softuni.mehana.orderAPI.model.entities.OrderEntityAPI;
import com.softuni.mehana.orderAPI.repository.OrderRepositoryAPI;
import com.softuni.mehana.orderAPI.service.OrderServiceAPI;
import com.softuni.mehana.orderAPI.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;

@Service
public class OrderServiceImplAPI implements OrderServiceAPI {

    private final OrderRepositoryAPI orderRepositoryAPI;
    private final ModelMapper modelMapper;

    public OrderServiceImplAPI(OrderRepositoryAPI orderRepositoryAPI, ModelMapper modelMapper) {
        this.orderRepositoryAPI = orderRepositoryAPI;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDetailsDto getOrderDetails(Long userId, Long id) {
        List<OrderEntityAPI> ordersByUser = orderRepositoryAPI.findAllByUserId(userId);
        try {
            return ordersByUser
                    .stream()
                    .filter(o -> o.getId().equals(id))
                    .findFirst()
                    .map(o -> mapToOrderDetailsDto(o))
                    .get();

        } catch (Exception e) {
            throw new ObjectNotFoundException();
        }
    }

    @Override
    public List<OrderDto> getAllOrders(Long userId) {
        List<OrderEntityAPI> ordersByUser = orderRepositoryAPI.findAllByUserId(userId);
        try {
            return ordersByUser
                    .stream()
                    .map(o -> mapToOrderDto(o))
                    .sorted(Comparator.comparing(OrderDto::getTime, Comparator.reverseOrder()))
                    .toList();

        } catch (Exception e) {
            throw new ObjectNotFoundException();
        }
    }

    @Override
    public OrderDetailsDto storeOrder(StoreOrderDto storeOrderDto) {
        OrderEntityAPI order = map(storeOrderDto);
        orderRepositoryAPI.save(order);
        return mapToOrderDetailsDto(order);
    }

    private OrderDto mapToOrderDto(OrderEntityAPI orderEntityAPI) {
        return modelMapper.map(orderEntityAPI, OrderDto.class);
    }

    private OrderDetailsDto mapToOrderDetailsDto(OrderEntityAPI orderEntityAPI) {
        return modelMapper.map(orderEntityAPI, OrderDetailsDto.class);
    }

    private OrderEntityAPI map(StoreOrderDto storeOrderDto) {
        OrderEntityAPI order = modelMapper.map(storeOrderDto, OrderEntityAPI.class);
        order.setId(null);
        return order;
    }

}
