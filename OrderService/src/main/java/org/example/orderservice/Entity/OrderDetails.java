package org.example.orderservice.Entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="order_details")

public class OrderDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Long orderId;
    @Column(name = "price")
    private Double price;
    @Column(name = "orderName")
    private String orderName;
    @Column(name = "quantity")
    private Long quantity;
}
