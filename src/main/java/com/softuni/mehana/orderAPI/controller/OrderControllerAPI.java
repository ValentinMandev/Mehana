package com.softuni.mehana.orderAPI.controller;

import com.softuni.mehana.orderAPI.model.dto.OrderDetailsDto;
import com.softuni.mehana.orderAPI.model.dto.OrderDto;
import com.softuni.mehana.orderAPI.model.dto.StoreOrderDto;
import com.softuni.mehana.orderAPI.service.OrderAPIService;
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


import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(
        name = "Orders",
        description = "The controller responsible for order storage."
)
public class OrderController {

    private final OrderAPIService orderAPIService;

    public OrderController(OrderAPIService orderAPIService) {
        this.orderAPIService = orderAPIService;
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
                .ok(orderAPIService.getOrderDetails(userId, id));
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
                .ok(orderAPIService.getAllOrders(userId));
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
        OrderDetailsDto orderDto = orderAPIService.storeOrder(storeOrderDto);
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
