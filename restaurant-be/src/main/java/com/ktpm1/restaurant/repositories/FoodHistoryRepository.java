package com.ktpm1.restaurant.repositories;

import com.ktpm1.restaurant.models.FoodHistory;
import com.ktpm1.restaurant.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodHistoryRepository extends JpaRepository<FoodHistory, Long> {
    List<FoodHistory> findByUser(User user);
}
