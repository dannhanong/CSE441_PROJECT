package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.Cart;
import com.ktpm1.restaurant.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);
}
