package com.mateo.ecom.backend.service;

import com.mateo.ecom.backend.models.AppOrder;
import com.mateo.ecom.backend.models.AppUser;
import com.mateo.ecom.backend.models.dao.OrderRepository;
import com.mateo.ecom.backend.models.dao.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<AppOrder> getOrders(AppUser user) {
        return orderRepository.findByUser(user);
    }
}
