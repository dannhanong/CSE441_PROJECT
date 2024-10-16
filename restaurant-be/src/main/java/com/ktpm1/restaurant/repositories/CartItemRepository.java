package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.Cart;
import com.ktpm1.restaurant.models.CartItem;
import com.ktpm1.restaurant.models.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Set<CartItem> findByCart(Cart cart);
    CartItem findByCartAndFood(Cart cart, Food food);
    void deleteByCart(Cart cart);
}
