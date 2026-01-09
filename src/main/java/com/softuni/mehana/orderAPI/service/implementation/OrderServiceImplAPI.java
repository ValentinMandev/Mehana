package com.softuni.mehana.orderAPI.service.implementation;

import com.softuni.mehana.orderAPI.model.dto.OrderDetailsDto;
import com.softuni.mehana.orderAPI.model.dto.OrderDto;
import com.softuni.mehana.orderAPI.model.dto.StoreOrderDto;
import com.softuni.mehana.orderAPI.model.entities.OrderEntity;
import com.softuni.mehana.orderAPI.repository.OrderAPIRepository;
import com.softuni.mehana.orderAPI.service.OrderAPIService;
import com.softuni.mehana.orderAPI.service.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;

@Service
public class OrderAPIServiceImpl implements OrderAPIService {

    private final OrderAPIRepository orderAPIRepository;
    private final ModelMapper modelMapper;

    public OrderAPIServiceImpl(OrderAPIRepository orderAPIRepository, ModelMapper modelMapper) {
        this.orderAPIRepository = orderAPIRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDetailsDto getOrderDetails(Long userId, Long id) {
        List<OrderEntity> ordersByUser = orderAPIRepository.findAllByUserId(userId);
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
        List<OrderEntity> ordersByUser = orderAPIRepository.findAllByUserId(userId);
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
        OrderEntity order = map(storeOrderDto);
        orderAPIRepository.save(order);
        return mapToOrderDetailsDto(order);
    }

    private OrderDto mapToOrderDto(OrderEntity orderEntity) {
        return modelMapper.map(orderEntity, OrderDto.class);
    }

    private OrderDetailsDto mapToOrderDetailsDto(OrderEntity orderEntity) {
        return modelMapper.map(orderEntity, OrderDetailsDto.class);
    }

    private OrderEntity map(StoreOrderDto storeOrderDto) {
        OrderEntity order = modelMapper.map(storeOrderDto, OrderEntity.class);
        order.setId(null);
        return order;
    }

}
