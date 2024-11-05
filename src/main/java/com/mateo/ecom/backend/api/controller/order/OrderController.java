package com.mateo.ecom.backend.api.controller.order;

import com.mateo.ecom.backend.models.AppOrder;
import com.mateo.ecom.backend.models.AppUser;
import com.mateo.ecom.backend.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<AppOrder> getOrders(@AuthenticationPrincipal AppUser user) {
        return orderService.getOrders(user);
    }
}
