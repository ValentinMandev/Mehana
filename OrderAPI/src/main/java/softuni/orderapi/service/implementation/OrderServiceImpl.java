package softuni.orderapi.service.implementation;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.orderapi.model.dto.OrderDetailsDto;
import softuni.orderapi.model.dto.OrderDto;
import softuni.orderapi.model.dto.StoreOrderDto;
import softuni.orderapi.model.entities.OrderEntity;
import softuni.orderapi.repository.OrderRepository;
import softuni.orderapi.service.OrderService;
import softuni.orderapi.service.exception.ObjectNotFoundException;

import java.util.Comparator;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderDetailsDto getOrderDetails(Long userId, Long id) {
        List<OrderEntity> ordersByUser = orderRepository.findAllByUserId(userId);
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
        List<OrderEntity> ordersByUser = orderRepository.findAllByUserId(userId);
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
        orderRepository.save(order);
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
