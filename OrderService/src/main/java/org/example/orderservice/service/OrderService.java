package org.example.orderservice.service;

import org.example.orderservice.Entity.OrderDetails;
import org.example.orderservice.Repo.OrderRepo;
import org.example.orderservice.dto.Order;
import org.example.orderservice.dto.OrderEvent;
import org.example.orderservice.dto.ProductDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {
    @Autowired
    OrderRepo orderRepo;
    @Value("${stock.service.name}")
    private String stockServiceBaseUrl;
    private final RestTemplate restTemplate;
    public final Logger log = LoggerFactory.getLogger(getClass());

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OrderDetails getOrderDetailsbyId(Long orderId) {
        return orderRepo.findById(orderId).orElse(null);
    }

    public OrderEvent createOrder(Long productId, Long quantity) {
        try {
            String url = "http://" + stockServiceBaseUrl + "/stockcontroller/getStockByProductId?productId=" + productId + "&quantity=" + quantity;
            log.info("Order creation URL: {}", url);
            ProductDetails productDetails = restTemplate.getForObject(url, ProductDetails.class);
            log.info("Order creation for Product ID: {}", productDetails);
            if (productDetails == null) {
                return null;
            }
            productDetails.setProductQuantity(quantity);
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrderName(productDetails.getProductName());
            orderDetails.setPrice(productDetails.getTotalPrice());
            orderDetails.setQuantity(quantity);
            OrderEvent orderEvent = new OrderEvent("order Event Created","Created",
                    new Order(orderDetails.getOrderId(), orderDetails.getPrice(), orderDetails.getOrderName(), orderDetails.getQuantity())
            ,productDetails);

            return orderEvent;
        }
        catch (Exception e) {
            log.info("Order creation failed for Product ID: {}", e.getMessage());
            return null;
        }
    }
}
