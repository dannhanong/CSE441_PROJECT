package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.Order;
import com.ktpm1.restaurant.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrder(Order order);
}
