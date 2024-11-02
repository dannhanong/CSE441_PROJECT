package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.Food;
import com.ktpm1.restaurant.models.Order;
import com.ktpm1.restaurant.models.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    OrderItem findByOrder(Order order);
    @Query("SELECT oi.food FROM OrderItem oi GROUP BY oi.food ORDER BY SUM(oi.quantity) DESC")
    Page<Food> findTop5ByOrderByQuantityDesc(PageRequest pageRequest);
}
