package com.softuni.mehana.controller;

import com.softuni.mehana.model.dto.CheckoutDto;
import com.softuni.mehana.model.dto.OrderDetailsDto;
import com.softuni.mehana.model.dto.OrderDto;
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

    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;

    public OrderController(UserService userService, CartService cartService, OrderService orderService) {
        this.userService = userService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/checkout")
    public String getFinalizePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getCurrentUser(userDetails);
        CartEntity cart = cartService.getCart(user);

        if (cart.getCartItemEntities().isEmpty()) {
            return "redirect:/cart";
        }

        Set<CartItemEntity> cartItemEntities = cartService.getCartItems(cart);

        model.addAttribute("checkoutDto", user.getUserInfo());
        model.addAttribute("price", cart.getPrice());
        model.addAttribute("cartItems", cartItemEntities);

        return "checkout";
    }

    @PostMapping("/checkout")
    public String checkout(@AuthenticationPrincipal UserDetails userDetails,
                           @Valid CheckoutDto checkoutDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("checkoutDto", checkoutDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.checkoutDto", bindingResult);
            return "checkout";
        }

        UserEntity user = userService.getCurrentUser(userDetails);
        orderService.storeOrder(user, checkoutDto);
        return "order/success";
    }

    @GetMapping("/orders/all")
    public String getOrderHistory(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<OrderDto> orders = orderService.getAllOrders(userDetails);
        model.addAttribute("orders", orders);
        return "order/history";
    }

    @GetMapping("/orders/{id}")
    public String getOrderDetails(@PathVariable("id") Long orderId,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        OrderDetailsDto order = orderService.getOrderDetails(userDetails, orderId);
        model.addAttribute("order", order);
        return "order/details";
    }

}
