package org.example.orderservice.service;

import org.example.orderservice.Entity.OrderDetails;
import org.example.orderservice.Repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    @Autowired
    OrderRepo orderRepo;

    public OrderDetails getOrderDetailsbyId(Long orderId) {
        return orderRepo.findById(orderId).orElse(null);
    }
}
