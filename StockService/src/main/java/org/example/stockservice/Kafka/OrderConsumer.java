package org.example.stockservice.Kafka;

import org.example.stockservice.Entity.ProductDetails;
import org.example.stockservice.Repo.ProductRepo;
import org.example.stockservice.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderConsumer {
    private static final Logger log = LoggerFactory.getLogger(OrderConsumer.class);
    public final ProductRepo productRepo;
    public OrderConsumer(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}" ,
    properties = {
        "spring.json.value.default.type=org.example.stockservice.dto.OrderEvent"
    })
    public void Consume(OrderEvent event) {
        log.info("order event recived in order cosumer"+event.toString());
        Long productId = event.getProductDetails().getProductId();
        Optional<ProductDetails> productDetails= productRepo.findByProductId(productId);
        if(productDetails.isPresent()) {
            ProductDetails product = productDetails.get();
            Long availableQuantity = product.getProductQuantity();
            Long requestedQuantity = event.getProductDetails().getProductQuantity();

            if (availableQuantity >= requestedQuantity) {
                product.setProductQuantity(availableQuantity - requestedQuantity);
                productRepo.save(product);
                log.info("Stock updated for Product ID: {}. New quantity: {}", productId, product.getProductQuantity());
            } else {
                log.error("Insufficient stock for Product ID: {}. Available: {}, Requested: {}",
                          productId, availableQuantity, requestedQuantity);
            }
        } else {
            log.error("Product with ID: {} not found in stock", productId);
        }
    }
}
