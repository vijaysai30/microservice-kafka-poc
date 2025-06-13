package org.example.orderservice.Repo;

import org.example.orderservice.Entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public  interface OrderRepo  extends JpaRepository<OrderDetails,Long> {
}
