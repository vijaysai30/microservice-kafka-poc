package org.example.orderservice.Controller;

import org.example.basedomain.Dto.Order;
import org.example.orderservice.Entity.OrderDetails;
import org.example.basedomain.Dto.OrderEvent;
import org.example.orderservice.Kafka.OrderProducer;
import org.example.orderservice.Repo.OrderRepo;
import org.example.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/ordercontroller")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderProducer orderProducer;

    @Autowired
    public OrderService orderService;
    @Autowired
    public OrderRepo orderRepo;

    @GetMapping("/check")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<OrderDetails>> getAllOrders() {
        List<OrderDetails> orderDetailsList= orderRepo.findAll();
        log.info("Retrieved {} orders", orderDetailsList.size());
        return ResponseEntity.status(HttpStatus.OK).body(orderDetailsList);
    }

    @GetMapping("/order")
    public ResponseEntity<OrderDetails> getOrderbyID(@RequestParam long orderId) {
        log.info("OrderController get order class ");

        OrderDetails order = orderService.getOrderDetailsbyId(orderId);
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        OrderEvent orderEvent = new OrderEvent("order Event Created","Created",
                new Order(order.getOrderId(), order.getPrice(), order.getOrderName(), order.getQuantity()));
        orderProducer.sendOrderEvent(orderEvent);
        return ResponseEntity.ok(order);
    }
}
