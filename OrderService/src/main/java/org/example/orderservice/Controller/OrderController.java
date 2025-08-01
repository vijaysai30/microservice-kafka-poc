package org.example.orderservice.Controller;

import org.example.orderservice.Entity.OrderDetails;
import org.example.orderservice.Kafka.OrderProducer;
import org.example.orderservice.Repo.OrderRepo;
import org.example.orderservice.dto.Order;
import org.example.orderservice.dto.OrderEvent;
import org.example.orderservice.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordercontroller")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    public OrderProducer orderProducer;

    @Autowired
    public OrderService orderService;
    @Autowired
    public OrderRepo orderRepo;

    @GetMapping("/getAllOrders")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<OrderDetails>> getAllOrders() {
        List<OrderDetails> orderDetailsList= orderRepo.findAll();
        log.info("Retrieved {} orders", orderDetailsList.size());
        return ResponseEntity.status(HttpStatus.OK).body(orderDetailsList);
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkOrders() {
        log.info("Checking orders");
        return ResponseEntity.ok().body("check");
    }

    @GetMapping("/get-order-databyId")
    @Cacheable(value = "order", key= "#orderId")
    public ResponseEntity<OrderDetails> getOrderbyID(@RequestParam long orderId) {
        log.info("OrderController get order class ");

        OrderDetails order = orderService.getOrderDetailsbyId(orderId);
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping("/create-order")
    public ResponseEntity<OrderDetails> createOrder(@RequestParam Long productId ,@RequestParam Long quantity) {
        log.info("Creating order for Product ID: {}", productId);
        OrderEvent orderEvent = orderService.createOrder(productId, quantity);
        if (orderEvent == null) {
            log.error("Order creation failed for Product ID: {}", productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Order order = orderEvent.getOrder();

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrderName(order.getOrderName());
        orderDetails.setPrice(order.getPrice());
        orderDetails.setQuantity(quantity);
        orderRepo.save(orderDetails);
        orderProducer.sendOrderEvent(orderEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDetails);
    }
}
