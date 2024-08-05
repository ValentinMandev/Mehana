package com.softuni.mehana.controller;

import com.softuni.mehana.model.dto.CheckoutDto;
import com.softuni.mehana.model.dto.OrderDetailsDto;
import com.softuni.mehana.model.dto.OrderDto;
import com.softuni.mehana.model.dto.StoreOrderDto;
import com.softuni.mehana.model.entities.CartEntity;
import com.softuni.mehana.model.entities.CartItemEntity;
import com.softuni.mehana.model.entities.UserEntity;
import com.softuni.mehana.service.CartService;
import com.softuni.mehana.service.OrderService;
import com.softuni.mehana.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
public class OrderController {

    UserService userService;
    CartService cartService;
    OrderService orderService;

    public OrderController(UserService userService, CartService cartService, OrderService orderService) {
        this.userService = userService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String getFinalizePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        CartEntity cart = cartService.getCart(userDetails);

        if (cart.getCartItemEntities().isEmpty()) {
            return "redirect:/cart";
        }

        UserEntity user = userService.getCurrentUser(userDetails);
        Set<CartItemEntity> cartItemEntities = cartService.getCartItems(cart);

        model.addAttribute("userInfo", user.getUserInfo());
        model.addAttribute("price", cart.getPrice());
        model.addAttribute("cartItems", cartItemEntities);

        return "checkout";
    }

    @PostMapping("/finalize-order")
    public String checkout(@Valid CheckoutDto checkoutDto, @AuthenticationPrincipal UserDetails userDetails) {

        UserEntity user = userService.getCurrentUser(userDetails);
        orderService.storeOrder(user, checkoutDto);
        return "order-successful";
    }

    @GetMapping("/orders/all")
    public String getOrderHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserEntity user = userService.getCurrentUser(userDetails);
        List<OrderDto> orders = orderService.getAllOrders(user.getId());

        model.addAttribute("orders", orders);
        return "/order-history";
    }

    @GetMapping("/orders/{id}")
    public String getOrderDetails(@PathVariable("id") Long orderId, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        UserEntity user = userService.getCurrentUser(userDetails);
        OrderDetailsDto order = orderService.getOrderDetails(user.getId(), orderId);

        model.addAttribute("order", order);
        return "/order-details";
    }

}
