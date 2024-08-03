package softuni.orderapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import softuni.orderapi.model.dto.OrderDetailsDto;
import softuni.orderapi.model.dto.OrderDto;
import softuni.orderapi.model.dto.StoreOrderDto;
import softuni.orderapi.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(
        name = "Orders",
        description = "The controller responsible for order storage."
)
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    @Operation(
            security = @SecurityRequirement(
                    name = "bearer-token"
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order details",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = OrderDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "404", description = "If order was not found",
                            content = {
                                    @Content(
                                            mediaType = "application/json"
                                    )
                            }
                    )
            }
    )
    @GetMapping("/{user_id}/{id}")
    public ResponseEntity<OrderDetailsDto> getById(@PathVariable("user_id") Long userId, @PathVariable("id") Long id) {
        return ResponseEntity
                .ok(orderService.getOrderDetails(userId, id));
    }




    @Operation(
            security = @SecurityRequirement(
                    name = "bearer-token"
            )
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of orders",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = OrderDto.class)
                                    )
                            }
                    ),
                    @ApiResponse(responseCode = "404", description = "If there are no orders",
                            content = {
                                    @Content(
                                            mediaType = "application/json"
                                    )
                            }
                    )
            }
    )
    @GetMapping("/all/{user_id}")
    public ResponseEntity<List<OrderDto>> getAll(@PathVariable("user_id") Long userId) {
        return ResponseEntity
                .ok(orderService.getAllOrders(userId));
    }





    @Operation(
            security = @SecurityRequirement(
                    name = "bearer-token"
            )
    )
    @PostMapping
    public ResponseEntity<OrderDetailsDto> storeOrder(
            @RequestBody StoreOrderDto storeOrderDto
    ) {
        OrderDetailsDto orderDto = orderService.storeOrder(storeOrderDto);
        return ResponseEntity.
                created(
                        ServletUriComponentsBuilder
                                .fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(orderDto.getId())
                                .toUri()
                ).body(orderDto);
    }
}
